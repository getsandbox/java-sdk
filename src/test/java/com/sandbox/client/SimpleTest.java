package com.sandbox.client;

import com.sandbox.common.models.ActivityMessage;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nickhoughton on 10/07/2015.
 */
public class SimpleTest {

    @Test
    @Ignore
    public void testCanGetActivities() throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", activeMQComponent("tcp://sandboxedge.com:61616"));
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody("request body");
        exchange.setPattern(ExchangePattern.InOut);
        Object response = context.createProducerTemplate().send("jms:queue:sbx.silent-pine-2394.request.queue", exchange);

        SandboxClient sandboxHelper = new SandboxClient("http://sandboxedge.com","api-58930768-1597-4ff4-ad16-c717e782491e");
        List<ActivityMessage> messages = sandboxHelper.getMessages(System.currentTimeMillis()-60000, "silent-pine-2394");

        assertThat(messages).extracting("messageObject.request.sandboxName").contains("silent-pine-2394");

    }
}
