import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PBM {
    private final int[] dy8 = {-1, -1, -1, 0, 1, 0, 1, 1, 1};
    private final int[] dx8 = {-1, 0, 1, 1, 1, -1, -1, 0, 1};
    private final int[] dy4 = {-1, 0, 1, 0};
    private final int[] dx4 = {0, -1, 0, 1};
    private final int width;
    private final int height;
    private final int[][] pixels;
    private final List<List<Position>> objects;
    private List<int[][]> isolatedObjects;
    private final List<List<Position>> holes;

    public PBM(int width, int height, int[][] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.objects = new ArrayList<>();
        this.holes = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    // método que retorna a altura da imagem em pixels.
    public int getHeight() {
        return height;
    }

    // método que retorna uma matriz de pixels que representa a imagem.
    public int[][] getPixels() {
        return pixels;
    }

    public static PBM loadImage(String path) throws IOException {
        FileReader file = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(file);

        if (!bufferedReader.readLine().equals("P1")) {
            System.err.println("O arquivo selecionado não é do tipo P1");
            System.exit(1);
        }

        bufferedReader.readLine();
        int[] dimensions = Arrays.stream(bufferedReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int[][] pixels = new int[dimensions[1]][dimensions[0]];

        for (int i = 0; i < dimensions[1]; i++) {
            pixels[i] = Arrays.stream(bufferedReader.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        PBM pbm = new PBM(dimensions[0], dimensions[1], pixels);

        bufferedReader.close();
        file.close();

        return pbm;
    }

    // método que retorna uma lista de objetos identificados.
    // Ele é responsável por percorrer a imagem pixel a pixel e identificar os objetos conectados que
    // tem  pixels com valor "1". Esse método usa a técnica de busca em profundidade (DFS) para identificar
    // todos os pixels conectados a um determinado pixel e adicioná-los a uma lista de matrizes de inteiros
    // que representa um objeto.
    public void identifyObjects() {

        boolean[][] visited = new boolean[height][width];


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {


                if (pixels[i][j] == 1 && !visited[i][j]) {
                    List<Position> object = new ArrayList<>();
                    identifyObjects(i, j, visited, object);
                    objects.add(object);
                }
            }
        }
    }

    //  método auxiliar chamado recursivamente pelo método identifyObjects() para identificar todos os
    // pixels conectados ao pixel atual e adicioná-los à lista de matrizes de inteiros "object"
    private void identifyObjects(int y, int x, boolean[][] visited, List<Position> object) {
        visited[y][x] = true;
        object.add(new Position(y, x));

        for (int i = 0; i < 8; i++) {
            int ny = y + dy8[i];
            int nx = x + dx8[i];


            if (ny >= 0 && ny < height && nx >= 0 && nx < width && pixels[ny][nx] == 1 && !visited[ny][nx]) {
                identifyObjects(ny, nx, visited, object);
            }
        }
    }

    // método que retorna a contagem dos objetos identificados
    public int countObjects() {
        return objects.size();
    }

    // método responsável por isolar cada objeto identificado em uma imagem binária,
    // preenchendo seus buracos e identificando-os. Ele utiliza o método "renderObject()"
    // para criar uma nova matriz que contém somente o objeto. Em seguida, ele utiliza os métodos
    // "fillHoles()" e "identifyHoles()" para preencher os buracos e identificá-los, respectivamente.
    public void isolateObjects() {
        isolatedObjects = objects.stream().map(this::renderObject).collect(Collectors.toList());
        isolatedObjects.forEach(this::fillHoles);
        isolatedObjects.forEach(this::identifyHoles);
    }


    // método que recebe uma lista de posições que correspondem a um objeto e retorna uma nova matriz
    // que contém apenas o objeto. A nova matriz é criada com base nas posições mínimas e máximas do
    // objeto, e o método preenche essa matriz com os valores "1" nas posições correspondentes às posições
    // do objeto.
    public int[][] renderObject(List<Position> Positions) {
        Position minI = Positions.stream().min(Comparator.comparingInt(Position::getI)).get();
        Position maxI = Positions.stream().max(Comparator.comparingInt(Position::getI)).get();
        Position minJ = Positions.stream().min(Comparator.comparingInt(Position::getJ)).get();
        Position maxJ = Positions.stream().max(Comparator.comparingInt(Position::getJ)).get();

        int[][] newObject = new int[maxI.getI() - minI.getI() + 1][maxJ.getJ() - minJ.getJ() + 1];
        for (int[] line : newObject) {
            Arrays.fill(line, -1);
        }

        Positions.forEach(Position -> newObject[Position.getI() - minI.getI()][Position.getJ() - minJ.getJ()] = 1);

        return newObject;
    }


    // método que retorna uma lista de furos identificados
    public void identifyHoles(int[][] pixels) {
        boolean[][] visited = new boolean[height][width];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {

                if (pixels[i][j] == 0 && !visited[i][j]) {
                    List<Position> hole = new ArrayList<>();
                    identifyHoles(i, j, visited, pixels, hole);
                    holes.add(hole);
                }
            }
        }
    }

    // método auxiliar chamado recursivamente pelo método identifyHoles() para identificar todos
    // os pixels conectados ao pixel atual e adicioná-los à lista de matrizes de inteiros "hole"
    private void identifyHoles(int y, int x, boolean[][] visited, int[][] pixels, List<Position> hole) {
        visited[y][x] = true;
        hole.add(new Position(y, x));

        for (int i = 0; i < 4; i++) {
            int ny = y + dy4[i];
            int nx = x + dx4[i];


            if (ny >= 0 && ny < pixels.length && nx >= 0 && nx < pixels[0].length && pixels[ny][nx] == 0 && !visited[ny][nx]) {
                identifyHoles(ny, nx, visited, pixels, hole);
            }
        }
    }

    public int countHoles() {
        return holes.size();
    }

    private void fillHoles(int[][] object) {
        for (int i = 0; i < object.length; i++) {
            int min = 0;
            int max = 0;

            for (int j = 0; j < object[0].length; j++) {
                if (object[i][j] == 1) {
                    min = j;
                    break;
                }
            }

            for (int j = 0; j < object[0].length; j++) {
                if (object[i][j] == 1) {
                    max = j;
                }
            }

            for (int j = min + 1; j < max; j++) {
                if (object[i][j] == -1) {
                    object[i][j] = 0;
                }
            }
        }
    }

    public int countObjectsWithHoles() {
        int count = 0;

        for (int[][] isolatedObject : isolatedObjects) {
            if (hasHole(isolatedObject)) {
                count++;
            }
        }

        return count;
    }

    public int countObjectsWithoutHoles() {
        int count = 0;

        for (int[][] isolatedObject : isolatedObjects) {
            if (!hasHole(isolatedObject)) {
                count++;
            }
        }

        return count;
    }


    // método responsável por verificar se um objeto isolado tem um furo identificado.
    // Ele utiliza a técnica de busca em profundidade (DFS) para identificar todos os pixels conectados
    // a um determinado pixel que tenha valor "0" e adicioná-los a uma lista de matrizes de inteiros que
    // representa um furo. Se a lista não estiver vazia, o método retorna true, indicando que o objeto
    // tem um furo. Se a lista estiver vazia, o método retorna false, indicando que o objeto não tem furos.
    public boolean hasHole(int[][] pixels) {
        boolean[][] visited = new boolean[height][width];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {

                if (pixels[i][j] == 0 && !visited[i][j]) {
                    List<Position> hole = new ArrayList<>();
                    identifyHoles(i, j, visited, pixels, hole);

                    if (!hole.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

