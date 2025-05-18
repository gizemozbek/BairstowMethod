import java.util.Arrays;

public class BairstowMethod {
    public static void bairstow(double[] a, double rr, double ss, double es, int maxit) {
        int nn = a.length - 1;
        double[] b = new double[nn + 1];
        double[] c = new double[nn + 1];
        double r = rr, s = ss;
        int iter = 0;
        double ea1 = 1, ea2 = 1;

        while (nn >= 3 && iter < maxit) {
            iter = 0;
            do {
                iter++;
                b[nn] = a[nn];
                b[nn-1] = a[nn-1] + r * b[nn];
                c[nn] = b[nn];
                c[nn-1] = b[nn-1] + r * c[nn];

                for (int i = nn-2; i >= 0; i--) {
                    b[i] = a[i] + r * b[i+1] + s * b[i+2];
                    c[i] = b[i] + r * c[i+1] + s * c[i+2];
                }

                double det = c[2] * c[2] - c[3] * c[1];
                if (Math.abs(det) > 1e-12) {
                    double dr = (-b[1] * c[2] + b[0] * c[3]) / det;
                    double ds = (-b[0] * c[2] + b[1] * c[1]) / det;
                    r += dr;
                    s += ds;

                    if (r != 0) ea1 = Math.abs(dr / r) * 100;
                    if (s != 0) ea2 = Math.abs(ds / s) * 100;
                } else {
                    r += 1;
                    s += 1;
                    iter = 0;
                }
            } while ((ea1 > es || ea2 > es) && iter < maxit);

            //Kökleri bul ve yazdır
            double[] roots = quadRoot(r, s);
            System.out.printf("Kökler: %.4f + %.4fi ve %.4f - %.4fi%n", roots[0], roots[1], roots[0], roots[1]);

            // Polinomu küçült (deflasyon)
            nn -= 2;
            a = Arrays.copyOf(b, nn + 1);
        }

        //Kalan polinom için kök bulma
        if (nn == 2) {
            r = -a[1] / a[2];
            s = -a[0] / a[2];
            double[] roots = quadRoot(r, s);
            System.out.printf("Kökler: %.4f + %.4fi ve %.4f - %.4fi%n", roots[0], roots[1], roots[0], roots[1]);
        } else if (nn == 1) {
            double root = -a[0] / a[1];
            System.out.printf("Kök: %.4f%n", root);
        }
    }

    //İkinci dereceden denklemin köklerini bulan fonksiyon
    public static double[] quadRoot(double r, double s) {
        double disc = r * r + 4 * s;
        double[] roots = new double[2];

        if (disc >= 0) {
            roots[0] = (r + Math.sqrt(disc)) / 2;
            roots[1] = (r - Math.sqrt(disc)) / 2;
        } else {
            roots[0] = r / 2;
            roots[1] = Math.sqrt(-disc) / 2;
        }
        return roots;
    }

    public static void main(String[] args) {
        //Polinom: x^5 - 3.5x^4 + 2.75x^3 + 2.125x^2 - 3.875x + 1.25
        double[] a = {1.25, -3.875, 2.125, 2.75, -3.5, 1};

        System.out.println("Bairstow Yöntemi ile Polinom Köklerini Bulma");
        System.out.println("Polinom: x^5 - 3.5x^4 + 2.75x^3 + 2.125x^2 - 3.875x + 1.25");

        double rr = -1; // Başlangıç r değeri
        double ss = -1; // Başlangıç s değeri
        double es = 0.01; // %1 hata toleransı
        int maxit = 100; // Maksimum iterasyon sayısı

        bairstow(a, rr, ss, es, maxit);
    }
}
