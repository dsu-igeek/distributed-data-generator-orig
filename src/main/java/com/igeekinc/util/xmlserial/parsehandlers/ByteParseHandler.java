/*
 * Copyright 2002-2014 iGeek, Inc.
 * All Rights Reserved
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
 
package com.igeekinc.util.xmlserial.parsehandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.igeekinc.util.xmlserial.XMLObjectParseHandler;
import com.igeekinc.util.xmlserial.exceptions.UnexpectedSubElementError;

public class ByteParseHandler implements XMLObjectParseHandler<Byte>
{
    private byte returnByte;
    private StringBuffer charInfo;
    private String elementName;
    private int radix;
    
    public ByteParseHandler()
    {
        charInfo = new StringBuffer(20);
        radix = 10;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
    {
        throw new UnexpectedSubElementError("Got a sub element for an Integer "+qName+"("+namespaceURI+":"+localName+")");
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
        returnByte = (byte)Integer.parseInt(charInfo.toString(), radix);
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        charInfo.append(ch, start, length);
    }
    
    public int getValue()
    {
        return returnByte;
    }
    
    public Byte getObject()
    {
        return new Byte(returnByte);
    }

    public void init(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
    {
        charInfo = new StringBuffer(20);
        elementName = qName;
        String radixStr = atts.getValue("radix");
        if (radixStr != null)
            radix = Integer.parseInt(radixStr);
        else
            radix = 10;
    }

    public String getElementName()
    {
        return elementName;
    }
}
