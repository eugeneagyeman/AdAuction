package dashboard;

import POJOs.ImpressionRecord;
import POJOs.Record;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dashboard.Filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// The root node of the tree is the whole data, unfiltered.
// Each child node is one extra layer of filtering.
// Reference the parent node to undo a filter level.
// FilterTree stores Multimaps of the data and filters when necessary.
public class FilterTree<T> {
    private Filter filters;
    private Node<T> root;
    private Node<T> current;

    public FilterTree(Filter filters, T rootData) {
        this.filters = filters;
        root = new Node<>();
        root.data = rootData;
        root.children = new ArrayList<>();
        root.setFilterPath(new ArrayList<>());
        this.setCurrentNode(root);
    }

    // filterID must be in the format: "filter_type,filter"
    public T filter(String filterID) throws Exception {
        // Sets current node to child if filter already exists
        for (Node<T> child : current.children) {
            if (child.getDeepestFilter().equals(filterID)) {
                this.setCurrentNode(child);
                return current.data;
            }
        }

        // If node is not already in children
        Node<T> node = new Node<>();
        node.data = this.selectFilter(filterID); // filter here based on filterID
        node.parent = this.current;
        node.children = new ArrayList<>();

        current.children.add(node);
        List<String> newPath = null;
        if (node.getFilterPath() == null)
            newPath = new ArrayList<>();
        else
            newPath = new ArrayList<>(node.getFilterPath());
        newPath.add(filterID);
        node.setFilterPath(newPath);
        this.setCurrentNode(node);
        return current.data;
    }

    private void filter(List<String> filterPath) throws Exception {
        for (String filterID : filterPath) {
            filter(filterID);
        }
    }

    public T filterDate(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        if (endDate.isBefore(startDate))
            throw new InvalidDateRangeException("End date should not be before start date"); //TO

        T newData = (T) filters.dateFilter(startDate, endDate);
        root = new Node<>();
        root.data = newData;
        root.children = new ArrayList<>();
        root.setFilterPath(new ArrayList<>());
        this.setCurrentNode(root);

        return root.data;
    }

    // filterID must be in the format: "filter_type,filter"
    // Throws exception when calling this on the root node
    public T undoFilter(String filterID) throws Exception {
        if (current.parent == null)
            throw new NullPointerException("Cannot undo filter from the root node.");
        // Checks whether the filter being removed is the current node
        if (filterID.equals(current.getDeepestFilter())) {
            this.setCurrentNode(current.parent);
        } else {
            List<String> newPath = new ArrayList<>(this.current.getFilterPath());
            newPath.remove(filterID);
            // Traverses the tree until either no node with the requested data has
            // been filtered yet, or until the desired filter path is reached, in which
            // case it sets the current node to that value
            this.setCurrentNode(root);
            for (String filter : newPath) {
                List<Node<T>> child = current.children.stream()
                        .filter(c -> c.getDeepestFilter().equals(filter))
                        .collect(Collectors.toList());
                if (child.isEmpty()) {
                    // Filter from the current node because the current filter does not yet exist
                    filter(newPath);
                } else if (child.size() == 1) {
                    this.setCurrentNode(child.get(0));
                } else
                    throw new Exception("Cannot have more than one of the same filter in a layer");
            }
        } return current.data;
    }

    private T selectFilter(String filterID) throws Exception {
        T data = null;
        Map<String, Collection<ImpressionRecord>> map = (Map<String, Collection<ImpressionRecord>>) current.data;

        String[] filterStr = filterID
                .replaceAll("\\s", "")
                .toLowerCase()
                .split(",");
        String filterType = filterStr[0];
        String filter = filterStr[1];

        switch (filterType) {
            case "age":
                data = (T) filters.impressionsAgeFilter(filter, map);
                break;
            case "income":
                //data = (T) filters.impressionsIncomeFilter(filter, map);
                break;
            case "gender":
                //data = (T) filters.impressionsGenderFilter(filter, map);
                break;
            case "context":
                //data = (T) filters.contextFilter(filter, map);
                break;
            default:
                throw new Exception("Incorrect format for filtering provided");
        }
        return data;
    }

    private synchronized void setCurrentNode(Node<T> node) {
        this.current = node;
    }

    public T getCurrentData() {
        return current.data;
    }

    static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
        private List<String> filterPath; // Identification for the node
        // String will be the filter (eg "age,<25") and will be passed when filtering

        // For use in filtering the child nodes for their data;
        public boolean getEquivalentData(T nodeData) {
            return this.data.equals(nodeData);
        } //TODO: May need to update equals to a more specific comparison

        public List<String> getFilterPath() {
            return this.filterPath;
        }

        public void setFilterPath(List<String> path) {
            this.filterPath = path;
        }

        public String getDeepestFilter() {
            return filterPath.get(filterPath.size() - 1);
        }
    }

    public static class InvalidDateRangeException extends Exception {
        InvalidDateRangeException(String message) {
            super(message);
        }
    }
}
