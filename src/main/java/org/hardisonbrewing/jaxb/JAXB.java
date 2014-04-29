/**
 * Copyright (c) 2010-2011 Martin M Reed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hardisonbrewing.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.codehaus.plexus.util.IOUtil;

public class JAXB {

    private static Hashtable<Class<?>, JAXBContext> jaxbContexts = new Hashtable<Class<?>, JAXBContext>();

    protected JAXB() {

        // do nothing
    }

    public static <T> T unmarshal( String xml, Class<T> clazz ) throws JAXBException {

        return unmarshal( xml.getBytes(), clazz );
    }

    public static <T> T unmarshal( byte[] bytes, Class<T> clazz ) throws JAXBException {

        return unmarshal( new ByteArrayInputStream( bytes ), clazz );
    }

    public static <T> T unmarshal( File file, Class<T> clazz ) throws JAXBException {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream( file );
            return unmarshal( inputStream, clazz );
        }
        catch (Exception e) {
            throw new JAXBException( e );
        }
        finally {
            IOUtil.close( inputStream );
        }
    }

    public static <T> T unmarshal( InputStream inputStream, Class<T> clazz ) throws JAXBException {

        try {
            Unmarshaller unmarshaller = getUnmarshaller( clazz );
            Object object = unmarshaller.unmarshal( inputStream );
            if ( object instanceof JAXBElement ) {
                JAXBElement<T> jaxbElement = (JAXBElement<T>) object;
                return (T) jaxbElement.getValue();
            }
            return (T) object;
        }
        catch (JAXBException e) {
            throw e;
        }
    }

    public static String marshal( Object object ) throws JAXBException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshal( outputStream, object );
        return outputStream.toString();
    }

    public static void marshal( File file, Object object ) throws JAXBException {

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream( file );
            marshal( outputStream, object );
        }
        catch (Exception e) {
            throw new JAXBException( e );
        }
        finally {
            IOUtil.close( outputStream );
        }
    }

    public static void marshal( OutputStream outputStream, Object object ) throws JAXBException {

        try {
            Marshaller marshaller = getMarshaller( object.getClass() );
            marshaller.marshal( object, outputStream );
        }
        catch (JAXBException e) {
            throw e;
        }
    }

    public static JAXBContext getJAXBContext( Class<?> clazz ) throws JAXBException {

        JAXBContext jaxbContext = jaxbContexts.get( clazz );
        if ( jaxbContext != null ) {
            return jaxbContext;
        }
        jaxbContext = JAXBContext.newInstance( clazz.getPackage().getName(), Thread.currentThread().getContextClassLoader() );
        jaxbContexts.put( clazz, jaxbContext );
        return jaxbContext;
    }

    public static Unmarshaller getUnmarshaller( Class<?> clazz ) throws JAXBException {

        return getJAXBContext( clazz ).createUnmarshaller();
    }

    public static Marshaller getMarshaller( Class<?> clazz ) throws JAXBException {

        return getJAXBContext( clazz ).createMarshaller();
    }
}
