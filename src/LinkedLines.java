public class LinkedLines {
    public Line firstLine;

    public void addLine(String str) {
        if(firstLine == null) {
            firstLine = new Line(str);
            return;
        }

        Line temp = firstLine;
        while (temp.next != null)
            temp = temp.next;

        temp.next = new Line(str);
    }

    public Line getNthLine(int n) {
        Line temp = firstLine;
        int i = 1;
        while(i++ != n)
            temp = temp.next;

        return temp;
    }


}
