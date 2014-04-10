package org.mixer2.sample.misc;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.AbstractJaxb;

/**
 * see https://groups.google.com/d/msg/mixer2-ja/vF6JEYWW5a0/MiRQm3SarHcJ
 *
 */
public class ReplaceSample01 {

    private static Mixer2Engine m2e = Mixer2EngineSingleton.getInstance();

    private String getAbsolutePath(String path) {
        path = getClass().getResource(path).toString();
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("file:/", "");
        } else {
            path = path.replaceFirst("file:", "");
        }
        return path;
    }
    
    @Test
    public void test() throws Exception {
        Html templateHtml = m2e.loadHtmlTemplate(new File(getAbsolutePath("template.html")));
        Html resultHtml = templateHtml.copy(Html.class);
        Html sourceHtml = m2e.loadHtmlTemplate(new File(getAbsolutePath("source.html")));
        Div sourceDiv = sourceHtml.getById("target", Div.class);
        List<Object> contents = sourceDiv.getById("here", Div.class).getContent();
        for (Object o : contents) {
            if (o instanceof AbstractJaxb) {
                AbstractJaxb aj = (AbstractJaxb) o;
                resultHtml.insertBeforeId("here", aj);
            } else {
                String s = (String) o;
                resultHtml.insertBeforeId("here", s);
            }
        }
        resultHtml.removeById("here");
        System.out.println(m2e.saveToString(resultHtml));
    }

}
