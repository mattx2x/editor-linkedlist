import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Editor {
    LinkedPages linkedPages;

    public Editor() {
        linkedPages = new LinkedPages();
    }

    public void parse(String address) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(address));
        scanner.useDelimiter("\\$");
        //scanner.useDelimiter("\\$");
        // constructing pages
        while(scanner.hasNext()) {
            linkedPages.addPage(scanner.next());
            if (scanner.hasNext())
                scanner.nextLine();
        }

    }

    public void save(String address) throws IOException {
        FileWriter fileWriter = new FileWriter(address);
        Page page = linkedPages.firstPage;
        while (page != null) {
            Line line = page.linkedLines.firstLine;
            while(line != null) {
                fileWriter.write(line.content + "\n");
                line = line.next;
            }

            fileWriter.write("$\n");
            page = page.next;
        }

        fileWriter.close();
    }

    public static void main(String[] args) throws IOException {
        String address = "./testCase1.txt";
        Editor editor = new Editor();
        Scanner sc = new Scanner(System.in);

        System.out.println("do you want continue with default address of the text file(" + address + ")?  y/n");
        String str = sc.next();
        if(str.equals("n")) {
            System.out.println("please enter the new address:");
            address = sc.next();
        }
        else if(!str.equals("y")) return;  // exit from program

        editor.parse(address);
        System.out.println("the parse method executed successfully! \n");

        while(true) {
            System.out.println("=========================== MENU ===========================");
            System.out.println(" where: where()\n next: nextPage()\n " +
                    "prev: previousPage()\n lines: lines()\n " +
                    "show: show(n)\n append: append(s)\n insert: insert(s,n)\n " +
                    "remove: remove(n)\n replace(n,s)\n swap: swap(m,n)\n " +
                    "find: find(s)\n findr: findAndReplace(s1,s2)\n " +
                    "undo: undo()\n redo: redo()\n s: save()\n x: exit without save\n " +
                    "xs: save and exit");
            System.out.println("============================================================");
            String command = sc.next();
            switch(command) {
                case "where":
                    System.out.println(editor.linkedPages.where());
                    break;
                case "next":
                    editor.linkedPages.nextPage();
                    break;
                case "prev":
                    editor.linkedPages.previousPage();
                    break;
                case "lines":
                    System.out.println(editor.linkedPages.currentPage.lines());
                    break;
                case "show":
                    System.out.println("enter n(number of lines you want to show):");
                    editor.linkedPages.currentPage.show(sc.nextInt());
                    break;
                case "append":
                    System.out.println("enter the string you want to append:");
                    System.out.println("Note: if you want to enter a multiple-lines string, use %");
                    sc.nextLine();
                    String s = sc.nextLine();
                    Scanner sc2 = new Scanner(s);
                    sc2.useDelimiter("%");
                    String s2 = "";
                    while(sc2.hasNext())
                        s2 += sc2.next() + "\n";

                    editor.linkedPages.currentPage.append(s2);
                    break;
                case "insert":
                    System.out.println("enter the string you want to insert:");
                    sc.nextLine();
                    s = sc.nextLine();

                    System.out.println("enter line number:");
                    editor.linkedPages.currentPage.insert(s, sc.nextInt());
                    break;
                case "remove":
                    System.out.println("enter number of the line you want to remove:");
                    editor.linkedPages.currentPage.remove(sc.nextInt());
                    break;
                case "replace":
                    System.out.println("enter number of the line you want to replace its string with the given one:");
                    int n = sc.nextInt();
                    System.out.println("enter the alternative string:");
                    editor.linkedPages.currentPage.replace(n, sc.next());
                    break;
                case "swap":
                    System.out.println("enter number of lines to swap(m and n):");
                    editor.linkedPages.currentPage.swap(sc.nextInt(), sc.nextInt());
                    break;
                case "find":
                    System.out.println("enter string to search:");
                    editor.linkedPages.find(sc.next());
                    break;
                case "findr":
                    System.out.println("enter string to search:");
                    String s1 = sc.next();
                    System.out.println("enter alternative string:");
                    editor.linkedPages.currentPage.findAndReplace(s1, sc.next());
                    break;
                case "undo":
                    editor.linkedPages.currentPage.undo();
                    break;
                case "redo":
                    editor.linkedPages.currentPage.redo();
                    break;
                case "s":
                    editor.save(address);
                    System.out.println("saved!");
                    break;
                case "x":
                    System.exit(0);
                    break;
                case "xs":
                    editor.save(address);
                    System.exit(0);
                    break;
                default:
                    System.out.println("invalid input. try again!");
                    break;
            }
        }
    }
}