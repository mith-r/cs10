public class DynamicArray {
    int[] array;
    int size;

    public DynamicArray(){
        array = new int[4];
        size = 0;
    }

    public void add(int value){
        if (size == array.length){
            System.out.println("Resizing");
            int[] copy = new int[size *2];
            for (int i = 0; i < array.length; i ++){
                copy[i] = array[i];
            }
            array = copy;
        }
        array[size] = value;
        size += 1;
    }

    public int get(int index) throws Exception{
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        return array[index];
    }

    public int size(){
        return size;
    }

    public static void main(String[] args) throws Exception{
        DynamicArray dynamicArray = new DynamicArray();

        dynamicArray.add(10);
        dynamicArray.add(20);
        dynamicArray.add(30);
        dynamicArray.add(40);
        dynamicArray.add(50); // Triggers resizing

        System.out.println(dynamicArray.size()); // Output: 5
        System.out.println(dynamicArray.get(4)); // Output: 50
        System.out.println(dynamicArray.get(0)); // Output
    }
}
