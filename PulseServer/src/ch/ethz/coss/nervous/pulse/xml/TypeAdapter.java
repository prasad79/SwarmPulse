/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *  *******************************************************************************/
package ch.ethz.coss.nervous.pulse.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import ch.ethz.coss.nervous.pulse.sql.SqlSetup;

public class TypeAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer v) throws Exception {
		switch (v) {
		case SqlSetup.TYPE_BOOL:
			return "BOOL";
		case SqlSetup.TYPE_INT32:
			return "INT32";
		case SqlSetup.TYPE_INT64:
			return "INT64";
		case SqlSetup.TYPE_FLOAT:
			return "FLOAT";
		case SqlSetup.TYPE_DOUBLE:
			return "DOUBLE";
		case SqlSetup.TYPE_STRING:
			return "STRING";
		case SqlSetup.TYPE_LOCATION:
			return "GEOGRAPHY";
		default:
			return "ERROR!";
		}
	}

	@Override
	public Integer unmarshal(String v) throws Exception {
		String type = v.toUpperCase();
		if (type.equals("BOOL")) {
			return SqlSetup.TYPE_BOOL;
		} else if (type.equals("INT32")) {
			return SqlSetup.TYPE_INT32;
		} else if (type.equals("INT64")) {
			return SqlSetup.TYPE_INT64;
		} else if (type.equals("FLOAT")) {
			return SqlSetup.TYPE_FLOAT;
		} else if (type.equals("DOUBLE")) {
			return SqlSetup.TYPE_DOUBLE;
		} else if (type.equals("STRING")) {
			return SqlSetup.TYPE_STRING;
		} else if (type.equals("GEOGRAPHY")) {
			return SqlSetup.TYPE_LOCATION;
		} else {
			return -1;
		}
	}
}
