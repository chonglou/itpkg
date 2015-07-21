package com.itpkg.core.web.widgets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-17.
 */
public class Form extends Response {
    public Form(String id, String name, String action) {
        this.id = id;
        this.name = name;
        this.method = "POST";
        this.action = action;
        this.fields = new ArrayList<>();
        this.buttons = new ArrayList<>();
    }

    public void addSubmit(String name) {
        addButton("submit", name, "primary");
    }

    public void addReset(String name) {
        addButton("reset", name, "default");
    }

    public void addButton(String id, String name, String style) {
        Button btn = new Button();
        btn.setId(id);
        btn.setName(name);
        btn.setStyle(style);
        buttons.add(btn);
    }

    public <T> void addField(Field field) {
        fields.add(field);
    }

    public void addEmailField(String id, String name, boolean required) {
        addEmailField(id, name, null, 0, required, null);
    }

    public void addEmailField(String id, String name, boolean required, String placeholder) {
        addEmailField(id, name, null, 0, required, placeholder);
    }

    public void addEmailField(String id, String name, String value, int size, boolean required, String placeholder) {
        EmailField tf = new EmailField(id, name);
        tf.setValue(value);
        tf.setSize(size > 0 ? size : 7);
        tf.setRequired(required);
        tf.setPlaceholder(placeholder);
        fields.add(tf);
    }

    public void addTextField(String id, String name, boolean required) {
        addTextField(id, name, null, 0, required, null);
    }

    public void addTextField(String id, String name, boolean required, String placeholder) {
        addTextField(id, name, null, 0, required, placeholder);
    }

    public void addTextField(String id, String name, String value, int size, boolean required, String placeholder) {
        TextField tf = new TextField(id, name);
        tf.setValue(value);
        tf.setSize(size > 0 ? size : 8);
        tf.setRequired(required);
        tf.setPlaceholder(placeholder);
        fields.add(tf);
    }

    public void addPasswordField(String id, String name, boolean required) {
        addPasswordField(id, name, 0, required, null);
    }

    public void addPasswordField(String id, String name, boolean required, String placeholder) {
        addPasswordField(id, name, 0, required, placeholder);
    }

    public void addPasswordField(String id, String name, int size, boolean required, String placeholder) {
        PasswordField pf = new PasswordField(id, name);
        pf.setSize(size > 0 ? size : 6);
        pf.setRequired(required);
        pf.setPlaceholder(placeholder);
        fields.add(pf);
    }

    private String id;
    private String name;
    private String action;
    private String method;
    private List<Field> fields;
    private List<Button> buttons;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
