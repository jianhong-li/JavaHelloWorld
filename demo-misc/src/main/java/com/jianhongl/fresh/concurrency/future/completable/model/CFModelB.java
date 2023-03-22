package com.jianhongl.fresh.concurrency.future.completable.model;

/**
 * @author lijianhong Date: 2023/3/20 Time: 7:29 PM
 * @version $
 */
public class CFModelB {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public CFModelB(int id) {
        this.id = id;
    }

    public CFModelB(String name) {
        this.name = name;
    }

    public CFModelB(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CFModelB{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
