/**
 * Copyright (c) 2011-2012 Martin M Reed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hardisonbrewing.signingserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;
import org.hardisonbrewing.jaxb.JAXB;
import org.hardisonbrewing.narst.Signer;
import org.hardisonbrewing.schemas.model.SigningAuthority;
import org.hardisonbrewing.schemas.model.SigningConfiguration;
import org.hardisonbrewing.schemas.model.SigningConfiguration.Files;
import org.json.JSONArray;
import org.json.JSONObject;

public class Application {

    public static void main( String[] args ) throws Exception {

        if ( args.length == 0 ) {
            throw new IllegalStateException( "Config location not specified" );
        }

        File config = getFile( new File( "." ), args[0] );
        SigningConfiguration signingConfiguration = getSigningConfiguration( config );

        SigningAuthority[] signingAuthorities = getSigningAuthorities( signingConfiguration );
        SigningResponse[] signingResponses = new SigningResponse[signingAuthorities.length];

        File[] files = getFiles( signingConfiguration, config.getParentFile() );

        for (int i = 0; i < signingAuthorities.length; i++) {
            signingResponses[i] = sign( signingConfiguration, files, signingAuthorities[i] );
        }

        submit( signingConfiguration, signingResponses );
    }

    private static File getFile( File directory, String filePath ) throws Exception {

        if ( filePath.startsWith( File.separator ) ) {
            return new File( filePath );
        }
        return new File( directory, filePath );
    }

    private static SigningConfiguration getSigningConfiguration( File file ) throws Exception {

        if ( !file.exists() ) {
            throw new IllegalStateException( "Config file not found: " + file );
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream( file );
            return JAXB.unmarshal( inputStream, SigningConfiguration.class );
        }
        finally {
            IOUtil.close( inputStream );
        }
    }

    private static SigningAuthority[] getSigningAuthorities( SigningConfiguration signingConfiguration ) throws Exception {

        SigningConfiguration.Authorities authorities = signingConfiguration.getAuthorities();
        if ( authorities == null ) {
            return null;
        }

        List<SigningAuthority> signingAuthorities = authorities.getAuthority();
        if ( signingAuthorities == null || signingAuthorities.isEmpty() ) {
            return null;
        }

        SigningAuthority[] _signingAuthorities = new SigningAuthority[signingAuthorities.size()];
        signingAuthorities.toArray( _signingAuthorities );
        return _signingAuthorities;
    }

    private static File[] getFiles( SigningConfiguration signingConfiguration, File directory ) throws Exception {

        Files files = signingConfiguration.getFiles();
        if ( files == null ) {
            return null;
        }

        List<String> filePaths = files.getFile();
        if ( filePaths == null || filePaths.isEmpty() ) {
            return null;
        }

        File[] _files = new File[filePaths.size()];
        for (int i = 0; i < _files.length; i++) {
            _files[i] = getFile( directory, filePaths.get( i ) );
        }
        return _files;
    }

    private static long getFileSize( File[] files ) {

        long size = 0;
        for (File file : files) {
            size += file.length();
        }
        return size;
    }

    private static SigningResponse sign( SigningConfiguration signingConfiguration, File[] files, SigningAuthority signingAuthority ) throws Exception {

        SigningResponse signingResponse = new SigningResponse();
        signingResponse.signingAuthority = signingAuthority;

        int success = 0;
        int failure = 0;
        int retry = 0;

        long duration = 0;

        for (int i = 0; i < files.length; i++) {

            boolean _success = false;
            int _retry;
            long _duration = 0;

            for (_retry = 0; !_success && _retry < 5;) {

                long start = System.currentTimeMillis();

                try {
                    _success = sign( signingConfiguration, signingAuthority, files[i] );
                }
                catch (Exception e) {
                    // do nothing
                }

                long end = System.currentTimeMillis();
                _duration += end - start;

                if ( !_success ) {
                    _retry++;
                }
            }

            duration += _duration / ( _retry + 1 );
            retry += _retry;

            if ( _success ) {
                success++;
            }
            else {
                failure++;
            }
        }

        signingResponse.success = success;
        signingResponse.failure = failure;
        signingResponse.retry = retry;
        signingResponse.duration = duration;
        signingResponse.size = getFileSize( files );
        signingResponse.count = files.length;
        return signingResponse;
    }

    private static final byte[] getPostData( SigningResponse[] signingResponses ) throws Exception {

        JSONObject submission = new JSONObject();
        submission.put( "date", System.currentTimeMillis() );
        submission.put( "os", System.getProperty( "os.name" ) );

        JSONArray signingResults = new JSONArray();
        submission.put( "results", signingResults );

        for (SigningResponse signingResponse : signingResponses) {
            JSONObject signingResult = new JSONObject();
            signingResult.put( "signerId", signingResponse.signingAuthority.getSignerId() );
            signingResult.put( "success", signingResponse.success );
            signingResult.put( "failure", signingResponse.failure );
            signingResult.put( "retry", signingResponse.retry );
            signingResult.put( "duration", signingResponse.duration );
            signingResult.put( "count", signingResponse.count );
            signingResult.put( "size", signingResponse.size );
            signingResults.put( signingResult );
        }

        return getByteArray( submission );
    }

    private static final byte[] getByteArray( JSONObject json ) throws Exception {

        ByteArrayOutputStream outputStream = null;
        Writer writer = null;
        try {
            outputStream = new ByteArrayOutputStream();
            writer = new OutputStreamWriter( outputStream );
            json.write( writer );
            writer.flush();
            return outputStream.toByteArray();
        }
        finally {
            IOUtil.close( outputStream );
            IOUtil.close( writer );
        }
    }

    private static final void submit( SigningConfiguration signingConfiguration, SigningResponse[] signingResponses ) throws Exception {

        byte[] postData = getPostData( signingResponses );
        System.out.println( "POST length[" + postData.length + "], data[" + new String( postData, "UTF-8" ) + "]" );

        URL url = new URL( signingConfiguration.getTracker() );
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setRequestMethod( "POST" );
        httpUrlConnection.setConnectTimeout( 5000 );
        httpUrlConnection.setDoOutput( true );
        httpUrlConnection.setRequestProperty( "Content-Type", "application/json" );

        OutputStream outputStream = null;
        try {
            outputStream = httpUrlConnection.getOutputStream();
            outputStream.write( postData );
            outputStream.flush();
        }
        finally {
            IOUtil.close( outputStream );
        }

        int responseCode = httpUrlConnection.getResponseCode();
        if ( responseCode < 200 || responseCode >= 300 ) {
            System.out.println( "Unable to submit to tracker because a response of \"" + responseCode + " " + httpUrlConnection.getResponseMessage() + "\" was returned." );
            return;
        }

        String response = getResponseString( httpUrlConnection );
        System.out.println( "RESPONSE length[" + response.length() + "], data[" + response + "]" );
    }

    private static final String getResponseString( HttpURLConnection httpUrlConnection ) throws Exception {

        InputStream inputStream = null;
        Reader reader = null;

        try {

            inputStream = httpUrlConnection.getInputStream();
            reader = new InputStreamReader( inputStream, "UTF-8" );

            StringBuffer stringBuffer = new StringBuffer();

            int n;
            char[] chars = new char[1024];
            while (( n = reader.read( chars ) ) != -1) {
                stringBuffer.append( chars, 0, n );
            }

            return stringBuffer.toString();
        }
        finally {
            IOUtil.close( inputStream );
            IOUtil.close( reader );
        }
    }

    private static boolean sign( SigningConfiguration signingConfiguration, SigningAuthority signingAuthority, File file ) throws Exception {

        Signer signer;

        try {
            Class<?> clazz = Class.forName( signingConfiguration.getSigner() );
            signer = (Signer) clazz.newInstance();

            String url = signingAuthority.getUrl();
            signer.setUrl( url == null || url.length() == 0 ? null : new URL( url ) );
            signer.setSignerId( signingAuthority.getSignerId() );
            signer.setClientId( signingAuthority.getClientId() );
            signer.setPassword( signingAuthority.getPassword() );
            signer.setInput( file );
            signer.setSalt( signingConfiguration.getSalt() );
            signer.setPrivateKey( signingConfiguration.getPrivateKey() );
            return signer.sign();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final class SigningResponse {

        public SigningAuthority signingAuthority;
        public int success;
        public int failure;
        public int retry;
        public long size;
        public long duration;
        public int count;
    }
}
