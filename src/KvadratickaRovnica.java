public class KvadratickaRovnica {
   public static double[] kvRovnica(double a, double b, double c){
      double d = (b * b) - (4 * a * c);

      if(d > 0.0){
         double x1 = (-b + Math.sqrt(d)) / (2.0 * a);
         double x2 = (-b - Math.sqrt(d)) / (2.0 * a);

         double[] ret = {d, x1, x2};
         return ret;
      }else if(d == 0.0){
         double x1 = -b / (2.0 * a);
         
         double[] ret = {d, x1};
         return ret;
      }

      return null;
   }
}
