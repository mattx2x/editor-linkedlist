import java.util.Scanner;

public class LinkedPages {
    public Page firstPage, currentPage;

    public void addPage(String text) {
        Scanner scanner = new Scanner(text);
        if(firstPage == null) {
            currentPage = firstPage = new Page();
            while(scanner.hasNext())
                firstPage.linkedLines.addLine(scanner.nextLine());

            return;
        }

        Page p = firstPage;
        while (p.next != null)
            p = p.next;
        Page q = new Page();
        while(scanner.hasNext())
            q.linkedLines.addLine(scanner.nextLine());
        q.prev = p;
        p.next = q;
    }

    public boolean isEmpty() {
        return firstPage == null;
    }

    public void nextPage() {
        if(currentPage.next == null) {
            System.out.println("there is not any page in the next !");
            return;
        }
        currentPage = currentPage.next;
    }

    public void previousPage() {
        if(currentPage.prev == null) {
            System.out.println("there is not any page in the previous !");
            return;
        }
        currentPage = currentPage.prev;
    }

    public int where() {
        Page temp = firstPage;
        int i = 1;
        while(temp != currentPage) {
            temp = temp.next;
            i++;
        }

        return i;
    }

    public void find(String str) {
        Page p = firstPage;
        int k = 1;
        while(p != null) {
            System.out.println("--------------------  page-" + k + "  --------------------");
            Line temp = p.linkedLines.firstLine;
            for (int i = 1; i <= p.lines(); i++) {
                if(temp.content.contains(str))
                    System.out.println("line-" + i + " : " + temp.content);
                temp = temp.next;
            }
            p = p.next;
            k++;
        }

    }

}
