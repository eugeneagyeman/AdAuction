package dashboard;

import POJOs.ImpressionRecord;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// The root node of the tree is the whole data, unfiltered.
// Each child node is one extra layer of filtering.
// Reference the parent node to undo a filter level.
// FilterTree stores Multimaps of the data and filters when necessary.
public class FilterTree {
    private Filter filters;

    public Map<String, Collection<ImpressionRecord>> getRootData() {
        return root.getData();
    }

    private Node root;
    private Node current;

    public FilterTree(Filter filters, Map<String, Collection<ImpressionRecord>> rootData) {
        this.filters = filters;
        root = new Node(rootData);
        root.children = new ArrayList<>();
        root.setFilterPath(new ArrayList<>());
        this.setCurrentNode(root);
    }

    // filterID must be in the format: "filter_type,filter"
    public Map<String, Collection<ImpressionRecord>> filter(String filterID) throws Exception {
        // Calls undoFilter if the filter is already in the filter path of the current node
        filterID = filterID.toLowerCase().replaceAll("\\s","");
        for (String str : current.getFilterPath()) {
            str = str.toLowerCase().replaceAll("\\s", "");
            if (filterID.equals(str))
                return undoFilter(filterID);
        }

        // Changes the filter if switching the filter within a particular filter type
        String[] filterList = filterID.split(",");
        for (String str : current.getFilterPath()) {
            String[] strList = str.toLowerCase().replaceAll("\\s", "").split(",");
            if (filterList[0].equals(strList[0])) {
                //System.out.println("FilterTree Node: " + current.data);
                undoFilter(str);
                //System.out.println(str);
                //System.out.println("FilterTree Data After Undo: " + current.data);
                //System.out.println("FilterTree Node After Undo: " + current.getFilterPath());
                //System.out.println(filterID);
                return filter(filterID);
            }
        }

        // Sets current node to child if filter already exists
        for (Node child : current.children) {
            if (child.getDeepestFilter().equals(filterID)) {
                this.setCurrentNode(child);
                return current.getData();
            }
        }

        // If node is not already in children
        Node node = new Node(this.selectFilter(filterID));
        node.parent = this.current;
        node.children = new ArrayList<>();

        current.children.add(node);
        List<String> newPath;
        if (current.getFilterPath() == null)
            newPath = new ArrayList<>();
        else
            newPath = new ArrayList<>(current.getFilterPath());
        newPath.add(filterID);
        node.setFilterPath(newPath);
        this.setCurrentNode(node);
        return current.getData();
    }

    private Map<String, Collection<ImpressionRecord>> filter(List<String> filterPath) throws Exception {
        Map<String, Collection<ImpressionRecord>> last = null;
        for (String filterID : filterPath) {
            last = filter(filterID);
        }
        return last;
    }

    public Map<String, Collection<ImpressionRecord>> filterDate(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        if (endDate.isBefore(startDate))
            throw new InvalidDateRangeException("End date should not be before start date");

        Map<String, Collection<ImpressionRecord>> newData = filters.dateFilter(startDate, endDate);
        root = new Node(newData);
        root.children = new ArrayList<>();
        root.setFilterPath(new ArrayList<>());
        this.setCurrentNode(root);

        return root.getData();
    }

    // filterID must be in the format: "filter_type,filter"
    // Throws exception when calling this on the root node
    public Map<String, Collection<ImpressionRecord>> undoFilter(String filterID) throws Exception {
        filterID = filterID.toLowerCase().replaceAll("\\s", "");
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
            if (newPath.size() == 0) {

            }
            for (String filter : newPath) {
                List<Node> child = current.children.stream()
                        .filter(c -> c.getDeepestFilter().equals(filter))
                        .collect(Collectors.toList());
                if (child.isEmpty()) {
                    // Filter from the current node because the current filter does not yet exist
                    return filter(newPath);
                } else if (child.size() == 1) {
                    this.setCurrentNode(child.get(0));
                } else
                    throw new Exception("Cannot have more than one of the same filter in a layer");
            }
        } return current.getData();
    }

    private Map<String, Collection<ImpressionRecord>> selectFilter(String filterID) throws Exception {
        //System.out.println("selectFilter: " + filterID);
        Map<String, Collection<ImpressionRecord>> data;
        Map<String, Collection<ImpressionRecord>> map = new HashMap<>(current.getData());

        String[] filterStr = filterID
                .replaceAll("\\s", "")
                .toLowerCase()
                .split(",");
        String filterType = filterStr[0];
        String filter = filterStr[1];

        switch (filterType) {
            case "age":
                data = filters.impressionsAgeFilter(filter, map);
                break;
            case "income":
                data = filters.impressionsIncomeFilter(filter, map);
                break;
            case "gender":
                data = filters.impressionsGenderFilter(filter, map);
                break;
            case "context":
                data = filters.contextFilter(filter, map);
                break;
            default:
                throw new Exception("Incorrect format for filtering provided");
        }
        return data;
    }

    private synchronized void setCurrentNode(Node node) {
        this.current = node;
    }

    public Map<String, Collection<ImpressionRecord>> getCurrentData() {
        return current.getData();
    }

    private static class Node {
        Map<String, Collection<ImpressionRecord>> data = null;
        Node parent;
        List<Node> children;
        List<String> filterPath; // Identification for the node

        Node(Map data) {
            this.data = data;
        }
        // String will be the filter (eg "age,<25") and will be passed when filtering

        public List<String> getFilterPath() {
            return this.filterPath;
        }

        public void setFilterPath(List<String> path) {
            this.filterPath = path;
        }

        public String getDeepestFilter() {
            return filterPath.get(filterPath.size() - 1);
        }

        public Map<String, Collection<ImpressionRecord>> getData() {
            return data;
        }
    }

    public static class InvalidDateRangeException extends Exception {
        InvalidDateRangeException(String message) {
            super(message);
        }
    }
}
