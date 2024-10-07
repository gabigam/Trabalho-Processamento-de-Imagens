import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Imagem 1: ");
        PBM image1 = PBM.loadImage("exemplo1.pbm");

        image1.identifyObjects();
        image1.isolateObjects();

        System.out.println("Total de objetos: " + image1.countObjects());
        System.out.println("Total de buracos: " + image1.countHoles());
        System.out.println("Total de objetos com buracos: " + image1.countObjectsWithHoles());
        System.out.println("Total de objetos sem buracos: " + image1.countObjectsWithoutHoles());

        System.out.println("Imagem 2: ");
        PBM image2 = PBM.loadImage("exemplo2.pbm");

        image2.identifyObjects();
        image2.isolateObjects();

        System.out.println("Total de objetos: " + image2.countObjects());
        System.out.println("Total de buracos: " + image2.countHoles());
        System.out.println("Total de objetos com buracos: " + image2.countObjectsWithHoles());
        System.out.println("Total de objetos sem buracos: " + image2.countObjectsWithoutHoles());

        System.out.println("Imagem 3: ");
        PBM image3 = PBM.loadImage("exemplo3.pbm");

        image3.identifyObjects();
        image3.isolateObjects();

        System.out.println("Total de objetos: " + image3.countObjects());
        System.out.println("Total de buracos: " + image3.countHoles());
        System.out.println("Total de objetos com buracos: " + image3.countObjectsWithHoles());
        System.out.println("Total de objetos sem buracos: " + image3.countObjectsWithoutHoles());

        System.out.println("Imagem 4: ");
        PBM image4 = PBM.loadImage("exemplo4.pbm");

        image4.identifyObjects();
        image4.isolateObjects();

        System.out.println("Total de objetos: " + image4.countObjects());
        System.out.println("Total de buracos: " + image4.countHoles());
        System.out.println("Total de objetos com buracos: " + image4.countObjectsWithHoles());
        System.out.println("Total de objetos sem buracos: " + image4.countObjectsWithoutHoles());

        System.out.println("Imagem 5: ");
        PBM image5 = PBM.loadImage("exemplo5.pbm");

        image5.identifyObjects();
        image5.isolateObjects();

        System.out.println("Total de objetos: " + image5.countObjects());
        System.out.println("Total de buracos: " + image5.countHoles());
        System.out.println("Total de objetos com buracos: " + image5.countObjectsWithHoles());
        System.out.println("Total de objetos sem buracos: " + image5.countObjectsWithoutHoles());
    }
}
