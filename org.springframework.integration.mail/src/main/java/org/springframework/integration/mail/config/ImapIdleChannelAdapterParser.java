/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.mail.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.Assert;

/**
 * Parser for the &lt;imap-idle-channel-adapter&gt; element in the 'mail' namespace.
 * 
 * @author Jonas Partner
 * @author Mark Fisher
 */
public class ImapIdleChannelAdapterParser extends AbstractSingleBeanDefinitionParser {

	private static final String BASE_PACKAGE = "org.springframework.integration.mail";


	protected String getBeanClassName(Element element) {
		return BASE_PACKAGE + ".ImapIdleChannelAdapter";
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String channel = element.getAttribute("channel");
		Assert.hasText(channel, "the 'channel' attribute is required");
		builder.addConstructorArgValue(this.parseImapMailReceiver(element, parserContext));
		builder.addPropertyReference("outputChannel", channel);
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "task-executor");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "auto-startup");
	}

	private BeanDefinition parseImapMailReceiver(Element element, ParserContext parserContext) {
		String uri = element.getAttribute("store-uri");
		Assert.hasText(uri, "the 'store-uri' attribute is required");
		BeanDefinitionBuilder receiverBuilder = BeanDefinitionBuilder.genericBeanDefinition(
				BASE_PACKAGE + ".ImapMailReceiver");
		receiverBuilder.addConstructorArgValue(uri);
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(receiverBuilder, element, "java-mail-properties");
		receiverBuilder.addPropertyValue("shouldDeleteMessages", element.getAttribute("should-delete-messages"));
		return receiverBuilder.getBeanDefinition();
	}

}
