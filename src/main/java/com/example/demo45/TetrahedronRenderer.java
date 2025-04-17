package com.example.demo45;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;


public class TetrahedronRenderer {

    public static MeshView createTetrahedronMesh(TetrahedronData data, double scale) {
        Point3D p1 = new Point3D(data.getV1(), data.getV2(), data.getV3()).multiply(scale);
        Point3D p2 = new Point3D(data.getV4(), data.getV5(), data.getV6()).multiply(scale);
        Point3D p3 = new Point3D(data.getV7(), data.getV8(), data.getV9()).multiply(scale);
        Point3D p4 = new Point3D(data.getV10(), data.getV11(), data.getV12()).multiply(scale);

        Color faceColor = (data.getColor() != null && !data.getColor().isBlank())
                ? Color.web(data.getColor())
                : Color.LIGHTBLUE;

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(
                (float) p1.getX(), (float) p1.getY(), (float) p1.getZ(),
                (float) p2.getX(), (float) p2.getY(), (float) p2.getZ(),
                (float) p3.getX(), (float) p3.getY(), (float) p3.getZ(),
                (float) p4.getX(), (float) p4.getY(), (float) p4.getZ()
        );

        mesh.getTexCoords().addAll(0, 0);

        mesh.getFaces().addAll(
                0, 0, 2, 0, 1, 0,
                0, 0, 3, 0, 2, 0,
                0, 0, 1, 0, 3, 0,
                1, 0, 2, 0, 3, 0
        );

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(faceColor));
        return meshView;
    }

    private static Group tetrahedronGroup; // Группа, содержащая текущий 3D-объект

    // Этот метод вызывается один раз для инициализации, например в MainApp при старте
    public static void setGroup(Group group) {
        tetrahedronGroup = group;
    }

    public static void updateScene(TetrahedronData data) {
        if (tetrahedronGroup == null || data == null) return;

        tetrahedronGroup.getChildren().clear();

        // Получаем координаты
        Point3D[] points = new Point3D[]{
                new Point3D(data.getV1(), data.getV5(), data.getV8()),   // A
                new Point3D(data.getV2(), data.getV6(), data.getV9()),   // B
                new Point3D(data.getV3(), data.getV7(), data.getV10()),  // C
                new Point3D(data.getV4(), data.getV11(), data.getV12())  // D
        };

        // Рисуем вершины
        for (Point3D p : points) {
            Sphere sphere = new Sphere(4); // радиус вершины
            sphere.setMaterial(new PhongMaterial(Color.RED));
            sphere.setTranslateX(p.getX());
            sphere.setTranslateY(p.getY());
            sphere.setTranslateZ(p.getZ());
            tetrahedronGroup.getChildren().add(sphere);
        }

        // Рисуем рёбра (все пары вершин)
        int[][] edges = {
                {0, 1}, {0, 2}, {0, 3},
                {1, 2}, {1, 3},
                {2, 3}
        };

        for (int[] edge : edges) {
            Point3D p1 = points[edge[0]];
            Point3D p2 = points[edge[1]];

            Line line = new Line();
            line.setStartX(p1.getX());
            line.setStartY(p1.getY());
            line.setEndX(p2.getX());
            line.setEndY(p2.getY());

            // Поскольку Line не учитывает Z, лучше использовать 3D линии с помощью цилиндров или MeshView
            javafx.scene.shape.Cylinder edgeLine = createEdge(p1, p2);
            tetrahedronGroup.getChildren().add(edgeLine);
        }
    }
    private static javafx.scene.shape.Cylinder createEdge(Point3D start, Point3D end) {
        Point3D diff = end.subtract(start);
        double height = diff.magnitude();

        Point3D mid = start.midpoint(end);

        javafx.scene.shape.Cylinder cylinder = new javafx.scene.shape.Cylinder(1.5, height);
        cylinder.setMaterial(new PhongMaterial(Color.BLUE));
        cylinder.setTranslateX(mid.getX());
        cylinder.setTranslateY(mid.getY());
        cylinder.setTranslateZ(mid.getZ());

        Point3D axisOfRotation = new Point3D(0, 1, 0).crossProduct(diff);
        double angle = Math.acos(diff.normalize().dotProduct(new Point3D(0, 1, 0)));
        angle = Math.toDegrees(angle);

        if (!axisOfRotation.equals(Point3D.ZERO)) {
            cylinder.setRotationAxis(axisOfRotation);
            cylinder.setRotate(angle);
        }

        return cylinder;
    }
}