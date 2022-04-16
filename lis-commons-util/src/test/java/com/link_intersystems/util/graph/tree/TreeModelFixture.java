package com.link_intersystems.util.graph.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@link TreeModelFixture} of the following structure:
 *
 * <pre>
 *                                     +-(Leaf1)
 *                                     |
 *                         +-(Branch1)-+
 *                         |           |
 *               +-(Tree1)-+           +-(Leaf2)
 *               |         |
 *               |         +-(Branch2)---(Leaf3)
 *               |
 *               |
 *      (Forest)-+-(Tree2)
 *               |
 *               |
 *               |
 *               |
 *               |         +-(Branch3)---(Leaf4)
 *               |         |
 *               +-(Tree3)-+-(Branch4)
 *                         |
 *                         +-(Branch5)---(Leaf5)
 * </pre>
 */
public class TreeModelFixture {

    public Forest forest;

    public Tree tree1;
    public Branch branch1;
    public Leaf leaf1;
    public Leaf leaf2;
    public Branch branch2;
    public Leaf leaf3;

    public Tree tree2;

    public Tree tree3;
    public Branch branch3;
    public Leaf leaf4;
    public Branch branch4;
    public Branch branch5;
    public Leaf leaf5;

    public TreeModelFixture() {
        forest = new Forest("Forest");

        tree1 = forest.addTree();
        tree2 = forest.addTree();
        tree3 = forest.addTree();

        branch1 = tree1.addBranch();
        branch2 = tree1.addBranch();
        branch3 = tree3.addBranch();
        branch4 = tree3.addBranch();
        branch5 = tree3.addBranch();

        leaf1 = branch1.addLeaf();
        leaf2 = branch1.addLeaf();
        leaf3 = branch2.addLeaf();
        leaf4 = branch3.addLeaf();
        leaf5 = branch5.addLeaf();
    }

    public static class NamedElement {
        private String name;

        public NamedElement() {
        }

        public NamedElement(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name == null ? getClass().getSimpleName() : name;
        }
    }

    public static class Forest extends NamedElement implements Iterable<Tree> {

        private List<Tree> trees = new ArrayList<>();

        public Forest() {
            super();
        }

        public Forest(String name) {
            super(name);
        }

        Tree addTree() {
            Tree tree = new Tree(toString() + "->" + "Tree " + (trees.size() + 1));
            trees.add(tree);
            return tree;
        }

        @Override
        public Iterator<Tree> iterator() {
            return trees.iterator();
        }
    }

    public static class Tree extends NamedElement implements Iterable<Branch> {

        private List<Branch> branches = new ArrayList<>();

        public Tree() {
            super();
        }

        public Tree(String name) {
            super(name);
        }

        Branch addBranch() {
            String name = toString() + "->Branch " + (branches.size() + 1);
            Branch branch = new Branch(name);
            branches.add(branch);
            return branch;
        }


        @Override
        public Iterator<Branch> iterator() {
            return branches.iterator();
        }
    }

    public static class Branch extends NamedElement implements Iterable<Leaf> {

        private List<Leaf> leaves = new ArrayList<>();

        public Branch() {
            super();
        }

        public Branch(String name) {
            super(name);
        }

        Leaf addLeaf() {
            Leaf leaf = new Leaf(toString() + "->Leaf " + (leaves.size() + 1));
            leaves.add(leaf);
            return leaf;
        }

        @Override
        public Iterator<Leaf> iterator() {
            return leaves.iterator();
        }
    }

    public static class Leaf extends NamedElement {

        public Leaf() {
            super();
        }

        public Leaf(String name) {
            super(name);
        }

    }
}