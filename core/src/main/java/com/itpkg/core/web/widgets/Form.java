package com.itpkg.core.web.widgets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public <T> void addField(Field field){
        fields.add(field);
    }

    public void addEmailField(String id, String name) {
        addEmailField(id, name, null, 0);
    }

    public void addEmailField(String id, String name, String value, int size) {
        EmailField tf = new EmailField(id, name);
        tf.setValue(value);
        tf.setSize(size > 0 ? size : 8);
        fields.add(tf);
    }

    public void addTextField(String id, String name) {
        addTextField(id, name, null, 0);
    }

    public void addTextField(String id, String name, String value, int size) {
        TextField tf = new TextField(id, name);
        tf.setValue(value);
        tf.setSize(size > 0 ? size : 8);
        fields.add(tf);
    }

    public void addPasswordField(String id, String name) {
        addPasswordField(id, name, 0);
    }

    public void addPasswordField(String id, String name, int size) {
        PasswordField pf = new PasswordField(id, name);
        pf.setSize(size > 0 ? size : 6);
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
