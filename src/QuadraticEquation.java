public class QuadraticEquation {
   //counts quadratic equaton and returns all roots and discriminant
   public static double[] qEquation(double a, double b, double c){
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

   //counts quadratic equation and returns user specified value
   public static double qEquation(double a, double b, double c, String arg){
      double d = (b * b) - (4 * a * c);
      //if user wants discriminant 
      if(arg == "d"){
         return d;
      }
      
      double x1 = 0, x2 = 0;

      if(d > 0.0){
         x1 = (-b + Math.sqrt(d)) / (2.0 * a);
         x2 = (-b - Math.sqrt(d)) / (2.0 * a);
      }else if(d == 0.0){
         x1 = -b / (2.0 * a);
         x2 = x1;
      }

      //if user wants x1
      if(arg =="x1"){
         return x1;
      //if user wants x2
      }else if(arg == "x2"){
         return x2;
      }

      return 0;
   }

   //finds Y coordinate for specified X value
   public static double findY(double a, double b, double c, double x){
      return (a * x * x) + (b * x) + c;
   }
}
