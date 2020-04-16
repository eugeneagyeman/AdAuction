package Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// The root node of the tree is the whole data, unfiltered.
// Each child node is one extra layer of filtering.
// Reference the parent node to undo a filter level.
// FilterTree stores Multimaps of the data and filters when necessary.
public class FilterTree<T> {
    private Node<T> root;
    private Node<T> current;

    public FilterTree(T rootData) {
        root = new Node<>();
        root.data = rootData;
        root.children = new ArrayList<>();
        this.setCurrentNode(root);
    }

    public void filter(T nodeData) {
        Node<T> node = new Node<>();
        node.data = nodeData;
        node.parent = this.current;
        node.children = new ArrayList<>();
        if (!current.children.contains(node))
            current.children.add(node);

        List<T> newPath = node.parent.getFilterPath();
        newPath.add(nodeData);
        node.setFilterPath(newPath);
        this.setCurrentNode(node);
    }

    public void filter(List<T> filterPath) {
        for (T nodeData : filterPath) {
            filter(nodeData);
        }
    }

    // Throws exception when calling this on the root node
    public void undoFilter(T nodeData) throws Exception {
        if (current.parent == null)
            throw new NullPointerException("Cannot undo filter from the root node.");
        // Checks whether the filter being removed is the current node
        if (nodeData.equals(current.data)) {
            this.setCurrentNode(current.parent);
        } else {
            List<T> newPath = this.current.getFilterPath();
            newPath.remove(nodeData);
            // Traverses the tree until either no node with the requested data has
            // been filtered yet, or until the desired filter path is reached, in which
            // case it sets the current node to that value
            while (!newPath.isEmpty()) {
                // Filters the children to check if one with the requested data is present
                List<Node<T>> child = current.children.stream()
                        .filter(c -> c.getEquivalentData(nodeData))
                        .collect(Collectors.toList());
                if (child.isEmpty()) {
                    // Filter from the current node because the current filter does not yet exist
                    filter(newPath);
                } else if (child.size() == 1) {
                    this.setCurrentNode(child.get(0));
                } else
                    throw new Exception("Cannot have more than one of the same filter in a layer");
            }
        }
    }

    private synchronized void setCurrentNode(Node<T> node) {
        this.current = node;
    }

    static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
        private List<T> filterPath; // Stored as the datatype for easy referencing

        // For use in filtering the child nodes for their data;
        public boolean getEquivalentData(T nodeData) {
            return this.data.equals(nodeData);
        }

        public List<T> getFilterPath() {
            return this.filterPath;
        }

        public void setFilterPath(List<T> path) {
            this.filterPath = path;
        }
    }
}
