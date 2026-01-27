package javaguides.functionalInterface;

@FunctionalInterface
public interface PaymentProcessor{
  
    // ✅ Single abstract method (this is the "function")
    boolean process(double amount);

    // ✅ Default method (allowed)
    default void log(double amount) {
        System.out.println("Processing payment of ₹" + amount);
    }

    // ✅ Static method (allowed)
    static void info() {
        System.out.println("PaymentProcessor Functional Interface");
    }

}
