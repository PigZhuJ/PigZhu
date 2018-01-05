package EffectiveJava.Builderpattern;

/**
 * Builder Pattern 当含有多个可选参数的时候可以使用这种设计模式
 */
public class NutritionFacts {
    private final int servingSize;
    private final int serving;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        //required parameters
        private final int servingSize;
        private final int serving;
        //optional parameter initialize to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int serving) {
            this.servingSize = servingSize;
            this.serving = serving;
        }

        public Builder colories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }
    }
    private  NutritionFacts(Builder builder){
        servingSize=builder.servingSize;
        serving=builder.serving;
        calories=builder.calories;
        fat=builder.fat;
        sodium=builder.sodium;
       carbohydrate=builder.carbohydrate ;
    }
}
