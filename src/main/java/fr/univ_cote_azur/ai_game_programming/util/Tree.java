package fr.univ_cote_azur.ai_game_programming.util;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private T data;
    private List<Tree<T>> children = new ArrayList<>();
    private Tree<T> parent = null;

    public Tree(T data) {
        this.data = data;
    }

    public void addChild(Tree<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        Tree<T> newChild = new Tree<>(data);
        this.addChild(newChild);
    }

    public void addChildren(List<Tree<T>> children) {
        for(Tree<T> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    public Tree<T> getParent() {
        return parent;
    }
}
