package com.company;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws IOException {

    PSO();
    }
    private static double sigmaP(double e,double e_dot,double T,double []a){
        double R=8.314;
        double W=Math.exp(-a[6]*e);
        return (W*a[0]*Math.pow(e,a[1])*Math.exp(a[3]/(R*(T+273)))+(1-W)*a[4]*Math.exp(a[5]/(R*(T+273))))*Math.pow(e_dot,a[2]);
    }
    private static double funkcja(Punkt p) throws IOException {
        double suma=0;
        File excelFile = new File("Doswiadczenia.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        for(int sheets=0;sheets<9;sheets++) {
            XSSFSheet sheet = workbook.getSheetAt(sheets);
            double T = sheet.getRow(1).getCell(3).getNumericCellValue();
            double e_dot = sheet.getRow(1).getCell(4).getNumericCellValue();
            double[] e = new double[21];
            double[] sigma_p = new double[21];
            for (int i = 0; i < 21; i++) {
                e[i] = sheet.getRow(i + 1).getCell(0).getNumericCellValue();
                sigma_p[i] = sheet.getRow(i + 1).getCell(1).getNumericCellValue();
                double tmp_sigmaP=sigmaP(e[i],e_dot,T,p.a);
                suma+=Math.abs(sigma_p[i]-tmp_sigmaP);
            }
        }
        return suma;
    }
    private static void PSO() throws IOException {
        double w=0.5;
        double c1=1;
        double c2=2;
        int m=50;
        Punkt []roj= new Punkt[m];
        Punkt []v=new Punkt[m];
        Punkt []P=new Punkt[m];
        Random random=new Random();

        for(int i=0;i<m;i++){
            double []a={random.nextDouble()*999+1,random.nextDouble(),random.nextDouble(),random.nextDouble()*9999+1,random.nextDouble(),random.nextDouble()*89999+1,random.nextDouble()};
            roj[i]=new Punkt(a);
            P[i]=new Punkt(roj[i]);
            double []b={0,0,0,0,0,0,0};
            v[i]=new Punkt(b);
        }
        Punkt G=new Punkt(roj[0]);
        for(int i=0;i<m;i++){
            if(funkcja(roj[i])<funkcja(G)){
                G=new Punkt(roj[i]);
            }
        }


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet= workbook.createSheet("Zmiennosc funkcji celu");
        XSSFSheet sheet2=workbook.createSheet("Wartosci_parametrow i funkcji celu");
        for(int j=0;j<100;j++){
            for(int i=0;i<m;i++){
                double r1=random.nextDouble();
                double r2=random.nextDouble();
                double []vp={0,0,0,0,0,0,0};
                for(int k=0;k<7;k++) {
                    vp[k] = w * v[i].a[k] + c1 * r1 * (P[i].a[k] - roj[i].a[k]) + c2 * r2 * (G.a[k] - roj[i].a[k]);
                }
                v[i]=new Punkt(vp);
                for(int k=0;k<7;k++) {
                    roj[i].a[k]+=v[i].a[k];
                }
                if(funkcja(roj[i])<funkcja(P[i])){
                    P[i]=new Punkt(roj[i]);
                }
                if(funkcja(roj[i])<funkcja(G)){
                    G=new Punkt(roj[i]);
                }

            }
            sheet.createRow(j).createCell(0).setCellValue(funkcja(G));
            System.out.println(funkcja(G));
        }
        sheet2.createRow(0).createCell(0).setCellValue("Wartosc funkcji celu");
        sheet2.getRow(0).createCell(1).setCellValue("Wartosci parametrow rownania");
        sheet2.createRow(1).createCell(0).setCellValue(funkcja(G));
        sheet2.getRow(1).createCell(1).setCellValue(G.a[0]);
        for(int i=1;i<7;i++){
            sheet2.createRow(i+1).createCell(1).setCellValue(G.a[i]);
        }
        FileOutputStream fis = new FileOutputStream("IOiM__cw4.xlsx");
        workbook.write(fis);


    }
}
