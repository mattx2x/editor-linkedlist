public class Function {
    enum Type {
        INSERT, // parameters[0]: String insertedString, parameters[1]: int lineNumber
        APPEND, // parameters[0]: String appendedString
        REMOVE, // parameters[0]: int lineNumber, parameters[1]: String removedString
        REPLACE, // parameters[0]: int lineNumber, parameters[1,2]: String firstStr & replacedStr
        SWAP,    // parameters[0]: int m, parameters[1]: int n
        FIND_REPLACE // parameters[0,1]: String firstStr & replacedStr
    }
    public Type type;
    public String[] parameters;
    public Function next;

    public Function(Type type, String... args) {
        this.type = type;
        parameters = args;
    }
}
