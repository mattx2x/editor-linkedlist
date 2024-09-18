import java.util.Scanner;

public class Page {   // Page is a linked-list with Line nodes
    public Page next, prev;
    public LinkedLines linkedLines;
    public ChangesStack undoStack, redoStack;

    public Page() {
        linkedLines = new LinkedLines();
        undoStack = new ChangesStack();
        redoStack = new ChangesStack();
    }

    private class ChangesStack {   // implementing stack using linked-list
        Function top;
        public void push(Function func) throws NullPointerException {
            if(func == null)
                throw new NullPointerException();
            Function temp = new Function(func.type, func.parameters);
            temp.next = top;
            top = temp;
        }
        public Function pop() {    // we will check emptiness in redo() or undo() method
            Function temp = top;
            top = top.next;
            return temp;
        }

        public void empty() {
            top = null;
        }
    }

    public void undo() {
        if(undoStack.top == null) {
            System.out.println("can't do undo anymore!");
            return;
        }

        Function lastFunc = undoStack.pop();
        redoStack.push(lastFunc);

        // execute the reverse methode of lastFunc
        switch(lastFunc.type) {
            case INSERT:
                notInsert(Integer.parseInt(lastFunc.parameters[1]));
                break;
            case APPEND:
                int n = lines() - lines(lastFunc.parameters[0]) + 1;
                notAppend(n);
                break;
            case REMOVE:
                notRemove(lastFunc.parameters[1], Integer.parseInt(lastFunc.parameters[0]));
                break;
            case REPLACE:
                notReplace(Integer.parseInt(lastFunc.parameters[0]), lastFunc.parameters[1]);
                break;
            case SWAP:
                notSwap(Integer.parseInt(lastFunc.parameters[1]), Integer.parseInt(lastFunc.parameters[0]));
                break;
            case FIND_REPLACE:
                notFindAndReplace(lastFunc.parameters[1], lastFunc.parameters[0]);
                break;
        }
    }

    public void redo() {
        if(redoStack.top == null) {
            System.out.println("can't do redo anymore!");
            return;
        }

        Function nextFunc = redoStack.pop();
        undoStack.push(nextFunc);

        // execute the nextFunc
        switch(nextFunc.type) {
            case INSERT:
                insert(nextFunc.parameters[0], Integer.parseInt(nextFunc.parameters[1]));
                break;
            case APPEND:
                append(nextFunc.parameters[0]);
                break;
            case REMOVE:
                remove(Integer.parseInt(nextFunc.parameters[0]));
                break;
            case REPLACE:
                replace(Integer.parseInt(nextFunc.parameters[0]), nextFunc.parameters[2]);
                break;
            case SWAP:
                swap(Integer.parseInt(nextFunc.parameters[0]), Integer.parseInt(nextFunc.parameters[1]));
                break;
            case FIND_REPLACE:
                findAndReplace(nextFunc.parameters[0], nextFunc.parameters[1]);
                break;
        }

    }

    private void setStacks(Function func) {
        undoStack.push(func);
    }

    private boolean isValidLineNumber(int n) {
        if(n > lines()  ||  n < 1) {
            System.out.println("the line-number is invalid !");
            return false;
        }
        return true;
    }

    public int lines() {
        int i = 0;
        Line temp = linkedLines.firstLine;
        while(temp != null) {
            temp = temp.next;
            i++;
        }

        return i;
    }

    private int lines(String s) {     // returns number of lines in the given string
        int i = 0;
        Scanner sc = new Scanner(s);
        while (sc.hasNext()) {
            sc.nextLine();
            i++;
        }
        return i;
    }

    public void show(int n) {
        if(!isValidLineNumber(n))
            return;

        Line temp = linkedLines.firstLine;
        int i = 0;
        while(i++ != n) {
            System.out.println(temp.content);
            temp = temp.next;
        }
    }

    public void insert(String str, int n) {
        if(!isValidLineNumber(n))
            return;

        if(n == 1) {
            Line newLine = new Line(str);
            newLine.next = linkedLines.firstLine;
            linkedLines.firstLine = newLine;
            setStacks(new Function(Function.Type.INSERT, str, "" + n));
            return;
        }

        Line temp = linkedLines.getNthLine(n-1);
        Line newLine = new Line(str);

        newLine.next = temp.next;
        temp.next = newLine;

        setStacks(new Function(Function.Type.INSERT, str, "" + n));
    }

    public void append(String str) {
        Scanner scanner = new Scanner(str);
        while(scanner.hasNext())
            linkedLines.addLine(scanner.next());

        setStacks(new Function(Function.Type.APPEND, str));
    }

    public void remove(int n) {
        if(!isValidLineNumber(n))
            return;

        Line temp;
        String removedStr;
        if(n == 1) {
            removedStr = linkedLines.firstLine.content;
            linkedLines.firstLine = linkedLines.firstLine.next;
        }
        else {
            temp = linkedLines.getNthLine(n-1);
            removedStr = temp.next.content;
            if(n == lines()) {
                temp.next = null;
                setStacks(new Function(Function.Type.REMOVE, "" + n, removedStr));
                return;
            }
            temp.next = temp.next.next;
        }
        setStacks(new Function(Function.Type.REMOVE, "" + n, removedStr));
    }

    public void replace(int n, String str) {
        if(!isValidLineNumber(n))
            return;

        String firstStr = linkedLines.getNthLine(n).content;
        linkedLines.getNthLine(n).content = str;

        setStacks(new Function(Function.Type.REPLACE, firstStr, str));
    }

    public void swap(int m, int n) {
        if(isValidLineNumber(n) && isValidLineNumber(m)) {
            Line lineM = linkedLines.getNthLine(m);
            Line lineN = linkedLines.getNthLine(n);
            String temp = lineM.content;

            lineM.content = lineN.content;
            lineN.content = temp;

            setStacks(new Function(Function.Type.SWAP, "" + m, "" + n));
        }
    }

    public void findAndReplace(String s1, String s2) {
        // traverse to right
        Page p = this;
        while(p != null) {
            Line temp = p.linkedLines.firstLine;
            while(temp != null){
                temp.content = temp.content.replaceAll(s1, s2);
                temp = temp.next;
            }
            p.setStacks(new Function(Function.Type.FIND_REPLACE, s1, s2));
            p = p.next;
        }

        // traverse to left
        p = this.prev;
        while(p != null) {
            Line temp = p.linkedLines.firstLine;
            while(temp != null){
                temp.content = temp.content.replaceAll(s1, s2);
                temp = temp.next;
            }
            p.setStacks(new Function(Function.Type.FIND_REPLACE, s1, s2));
            p = p.prev;
        }
    }

    private void notInsert(int n) {     // notMethod is reverse of method
        Line temp;
        if(n == 1)
            linkedLines.firstLine = linkedLines.firstLine.next;

        else {
            temp = linkedLines.getNthLine(n-1);
            if(n == lines()) {
                temp.next = null;
                return;
            }
            temp.next = temp.next.next;
        }
    }

    private void notAppend(int n) {     // this will remove all lines after line n-1
        if(n == 1) {
            linkedLines.firstLine = null;
            return;
        }

        Line temp = linkedLines.getNthLine(n-1);
        temp.next = null;
    }

    private void notRemove(String str, int n) {
        if(n == 1) {
            Line newLine = new Line(str);
            newLine.next = linkedLines.firstLine;
            linkedLines.firstLine = newLine;
            return;
        }

        Line temp = linkedLines.getNthLine(n-1);
        Line newLine = new Line(str);

        newLine.next = temp.next;
        temp.next = newLine;
    }

    private void notReplace(int n, String str) {
        linkedLines.getNthLine(n).content = str;
    }

    private void notSwap(int m, int n) {
        Line lineM = linkedLines.getNthLine(m);
        Line lineN = linkedLines.getNthLine(n);
        String temp = lineM.content;

        lineM.content = lineN.content;
        lineN.content = temp;
    }

    private void notFindAndReplace(String s1, String s2) {
        // traverse to right
        Page p = this;
        while(p != null) {
            Line temp = p.linkedLines.firstLine;
            while(temp != null){
                temp.content = temp.content.replaceAll(s1, s2);
                temp = temp.next;
            }
            p = p.next;
        }

        // traverse to left
        p = this.prev;
        while(p != null) {
            Line temp = p.linkedLines.firstLine;
            while(temp != null){
                temp.content = temp.content.replaceAll(s1, s2);
                temp = temp.next;
            }
            p = p.prev;
        }
    }

}
