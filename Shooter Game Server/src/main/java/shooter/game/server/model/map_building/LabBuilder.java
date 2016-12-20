/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shooter.game.server.model.map_building;

/**
 * @author ILMAZ
 */
public class LabBuilder {
    private final int x;
    private final int y;
    private int k = 10;
    private final Point[][] p;
    private int k1, k2;


    public LabBuilder(int x, int y) {
        this.x = x;
        this.y = y;
        p = new Point[x][y + 1];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y + 1; j++) {
                p[i][j] = new Point();
            }
        }

    }

    public Point[][] build() {
        //firs line
        for (int i = 0; i < x; i++) {
            p[i][0].n = k;
            k++;
        }

        for (int j = 0; j < y; j++) {
            boolean[] dp = new boolean[x * y + 10];
            for (int i = 0; i < x; i++) {

                //right
                if (i != x - 1) {
                    if (rand()) {
                        p[i][j].r();
                    } else {
                        if (p[i][j].n > p[i + 1][j].n) {
                            k1 = p[i][j].n;
                            k2 = p[i + 1][j].n;
                        } else {
                            k1 = p[i + 1][j].n;
                            k2 = p[i][j].n;
                        }
                        for (int j1 = j; j1 <= j + 1; j1++) {
                            for (int i1 = 0; i1 < x; i1++) {
                                if (p[i1][j1].n == k1) {
                                    p[i1][j1].n = k2;
                                }
                            }
                        }
                    }
                } else {
                    p[i][j].r();
                }
                //down
                if (j != y - 1) {
                    if (rand()) {
                        p[i][j].d();
                        p[i][j + 1].n = k;
                        k++;
                    } else {
                        p[i][j + 1].n = p[i][j].n;
                        dp[p[i][j].n] = false;
                    }
                } else {
                    p[i][j].d();
                }
//                print();
            }
            //not down way test
            for (int i = 0; i < x; i++) {
                if (!dp[p[i][j].n] && j != y - 1) {
                    if (p[i][j].d) {
                        p[i][j].d();
                        p[i][j + 1].n = p[i][j].n;
                    }
                    dp[p[i][j].n] = true;
                }
            }
        }
        //last line
        int j = y - 1;
        for (int i = 0; i < x - 1; i++) {
            if (p[i][j].n != p[i + 1][j].n && p[i][j].r) {
                if (p[i][j].n > p[i + 1][j].n) {
                    k1 = p[i][j].n;
                    k2 = p[i + 1][j].n;
                } else {
                    k1 = p[i + 1][j].n;
                    k2 = p[i][j].n;
                }
                for (int i1 = 0; i1 < x; i1++) {
                    if (p[i1][j].n == k1) {
                        p[i1][j].n = k2;
                    }
                }
                p[i][j].r();
            }
        }
        print();
        return p;
    }

    private boolean rand() {
        return ((int) Math.round(Math.random() * 100)) % 4 == 0;
    }

    private void print() {
        for (int i = 0; i < x; i++) {
            System.out.print(" __");
        }
        System.out.println();
        for (int i = 0; i < y; i++) {
            System.out.print("|");
            for (int j = 0; j < x; j++) {
                if (p[j][i].d) {
                    System.out.print("__");//"+p[j][i].n+"
                } else {
                    System.out.print("  ");
                }
                if (p[j][i].r) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public boolean[][] getAsLocation(int scale) {
        int locX = x * scale + 1;
        int locY = y * scale + 1;
        boolean[][] location = new boolean[locX][locY];
        for (int i = 0; i < locX; i++) {
            location[i][0] = true;
        }
        for (int i = 0; i < locY; i++) {
            location[0][i] = true;
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                pointToLocation(j, i, p[j][i], location, scale);
            }
        }
        return location;
    }

    private void pointToLocation(int x, int y, Point point, boolean[][] location, int scale) {
        int locX = x * scale + 1;
        int locY = y * scale + 1;
        for (int i = 0; i < scale - 1; i++) {
            for (int j = 0; j < scale - 1; j++) {
                location[locX + j][locY + i] = false;
            }
            location[locX + scale - 1][locY + i] = point.r;
        }
        for (int i = 0; i < scale - 1; i++) {
            location[locX + i][locY + scale - 1] = point.d;
        }
        location[locX + scale - 1][locY + scale - 1] = point.r || point.d;
    }
}
