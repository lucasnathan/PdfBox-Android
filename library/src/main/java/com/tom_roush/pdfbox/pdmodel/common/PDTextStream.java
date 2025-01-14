/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tom_roush.pdfbox.pdmodel.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.cos.COSString;
import com.tom_roush.pdfbox.io.IOUtils;

/**
 * A PDTextStream class is used when the PDF specification supports either
 * a string or a stream for the value of an object.  This is usually when
 * a value could be large or small, for example a JavaScript method.  This
 * class will help abstract that and give a single unified interface to
 * those types of fields.
 *
 * @author Ben Litchfield
 */
public class PDTextStream implements COSObjectable
{
    private COSString string;
    private COSStream stream;

    /**
     * Constructor.
     *
     * @param str The string parameter.
     */
    public PDTextStream( COSString str )
    {
        string = str;
    }

    /**
     * Constructor.
     *
     * @param str The string parameter.
     */
    public PDTextStream( String str )
    {
        string = new COSString( str );
    }

    /**
     * Constructor.
     *
     * @param str The stream parameter.
     */
    public PDTextStream( COSStream str )
    {
        stream = str;
    }

    /**
     * This will create the text stream object.  base must either be a string
     * or a stream.
     *
     * @param base The COS text stream object.
     *
     * @return A PDTextStream that wraps the base object.
     */
    public static PDTextStream createTextStream( COSBase base )
    {
        PDTextStream retval = null;
        if( base instanceof COSString )
        {
            retval = new PDTextStream( (COSString) base );
        }
        else if( base instanceof COSStream )
        {
            retval = new PDTextStream( (COSStream)base );
        }
        return retval;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSBase getCOSObject()
    {
    	return string == null ? stream : string;
    }

    /**
     * This will get this value as a string.  If this is a stream then it
     * will load the entire stream into memory, so you should only do this when
     * the stream is a manageable size.
     *
     * @return This value as a string.
     *
     * @throws IOException If an IO error occurs while accessing the stream.
     */
    public String getAsString() throws IOException
    {
        if (string != null) 
        {
            return string.getString();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream is = stream.createInputStream();
        IOUtils.copy(is, out);
        IOUtils.closeQuietly(is);
        return new String(out.toByteArray(), "ISO-8859-1");
    }

    /**
     * This is the preferred way of getting data with this class as it uses
     * a stream object.
     *
     * @return The stream object.
     *
     * @throws IOException If an IO error occurs while accessing the stream.
     */
    public InputStream getAsStream() throws IOException
    {
        InputStream retval;
        if( string != null )
        {
            retval = new ByteArrayInputStream( string.getBytes() );
        }
        else
        {
            retval = stream.createInputStream();
        }
        return retval;
    }
}
