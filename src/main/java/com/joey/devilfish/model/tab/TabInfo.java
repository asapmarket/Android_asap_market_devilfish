package com.joey.devilfish.model.tab;

/**
 * 文件描述
 * Date: 2018/3/13
 *
 * @author xusheng
 */

public class TabInfo {

    public int id;

    private int icon;

    private String name;

    private Class fragmentClass;

    public TabInfo(int id) {
        this.id = id;
    }

    public TabInfo(int id, String name, Class clazz) {
        this(id, name, 0, clazz);
    }

    public TabInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TabInfo(int id, String name, int icon, Class clazz) {
        this.name = name;
        this.id = id;
        this.icon = icon;
        fragmentClass = clazz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
