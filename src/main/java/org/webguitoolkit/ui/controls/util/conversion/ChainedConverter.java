/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG 
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied. See the License for the specific language governing permissions 
and limitations under the License.
*/ 
package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
/**
 * <pre>
 * A meta-converter, which actually lets you using multiple converters, by chaining them
 * All converters will be executed in given order. So the output of the first
 * converter is the input of the next.
 * Expecially useful if you combine inputvalidation with formatting.
 * </pre>
 * 
 * No Converter in the chain must be null!
 * @author arno
 *
 */
public class ChainedConverter implements IConverter {
	// the actuall converter being executed
	protected IConverter[] chain;
	/**
	 * create a converter which uses 'chain' as converter chain.
	 * @param chain
	 */
	public ChainedConverter(IConverter[] chain) {
		super();
		this.chain = chain;
	}

	/**
	 * create a new chain, you must call setConverterChain()
	 *
	 */
	public ChainedConverter(){}
	
	/**
	 * convenient contructor , builds a chain of two converters.
	 * 
	 */
	public ChainedConverter(IConverter con1, IConverter con2) {
		chain = new IConverter[]{ con1, con2 };
	}
	
	public void setConverterChain(IConverter[] theChain) {
		chain = theChain;
	}

	public Object parse(String textRep) throws ConversionException {
		Object tr = textRep;
		for (int i=0;i<chain.length;i++) {
			tr = chain[i].parse( (String) tr);
		}
		return tr;
	}

	public Object convert(Class type, Object value) {
		for (int i=0;i<chain.length;i++) {
			value = chain[i].convert(type, value);
		}
		return value;
	}

}
