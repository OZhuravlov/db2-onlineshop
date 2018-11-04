package com.study.onlineshop.web.templater;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;


public class PageGenerator {
    private static final String HTML_DIR = "templates";

    private static PageGenerator pageGenerator;

    private final Configuration cfg;

    public static PageGenerator instance(){
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(filename + ".html");
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return stream.toString();
    }

    private PageGenerator() {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(), "/" + HTML_DIR);
    }

    public Configuration getCfg() {
        return cfg;
    }

}
