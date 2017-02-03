/*
 * Created on Fri Feb 03 14:44:25 CET 2017
*/
package neural1.networks;

import neural1.Network;

public class XOR {

private static final int inputCount = 2;
private static final int hiddenCount = 3;
private static final int outputCount = 1;

private static final double[] matrix = { 5.22274174174222,
    -5.243993933524524,
    7.150696756822094,
    -7.233462064837514,
    5.284755147063974,
    -4.9557444283372964,
    -6.913109151353453,
    12.751608225534232,
    -6.4192448858227};

private static final double[] thresholds = { 0.40171159289403324,
    -0.15007657304054978,
    2.7360872665749625,
    -3.7139274817660386,
    2.4902566322250257,
    6.529298799477571};


  public static Network getNetwork(double learnRate, double momentum) {
    Network net = new Network(inputCount, hiddenCount, outputCount, learnRate, momentum);
    net.setMatrix(matrix);
    net.setThresholds(thresholds);
    return net;
  }


}
