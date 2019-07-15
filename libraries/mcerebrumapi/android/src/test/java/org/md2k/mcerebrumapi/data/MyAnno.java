package org.md2k.mcerebrumapi.data;

public class MyAnno extends MCAnnotation{
    int a;
    double b;

    public MyAnno(int a, double b) {
        super(0,1);
        this.a = a;
        this.b = b;
    }
}
