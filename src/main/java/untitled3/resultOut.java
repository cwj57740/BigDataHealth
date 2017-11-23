package untitled3;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.joone.engine.Pattern;
import org.joone.io.StreamOutputSynapse;
import org.joone.net.NetCheck;

import java.util.TreeSet;

public class resultOut extends StreamOutputSynapse {
    private String FileName = "";
    private boolean append = false;
   // protected transient PrintWriter printer = null;
    private static final long serialVersionUID = 3194671306693862830L;
    double result;
    public resultOut() {
    }

    public synchronized void write(Pattern pattern) {
      //  if(this.printer == null || pattern.getCount() == 1) {
      //      this.setFileName(this.FileName);
      //  }

        if(pattern.getCount() == -1) {
            this.flush();
        } else {
            double[] array = pattern.getArray();

            for(int i = 0; i < array.length; ++i) {
             //   System.out.println("array[i]="+array[i]+"  ");
             //   this.printer.write(array[i]+"");
                result=array[i];
                if(i < array.length - 1) {
             //       this.printer.print(this.getSeparator());
                }
            }

          //  this.printer.println();
        }

    }
    public double getResult()
    {
        return result;
    }
    public String getFileName() {
        return this.FileName;
    }

    public void setFileName(String fn) {
        this.FileName = fn;

    }

    public void flush() {

    }

    public TreeSet check() {
        TreeSet checks = super.check();
        if(this.FileName == null || this.FileName.trim().equals("")) {
            checks.add(new NetCheck(0, "File Name not set.", this));
        }

        return checks;
    }

    public boolean isAppend() {
        return this.append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

}
