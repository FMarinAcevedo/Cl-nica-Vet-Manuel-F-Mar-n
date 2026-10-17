# Hospital Veterinario

Este es mi proyecto para la evaluación de Programación Orientada a Objetos en Java. Es un programa de consola para manejar los pacientes de una clínica veterinaria: se pueden ingresar perros, gatos y aves exóticas, vacunarlos, asignarles una jaula, medicarlos y darlos de alta.

Todo se guarda en vectores de tamaño fijo (`Paciente[10]` y `Jaula[6]`), sin `ArrayList`, sin archivos y sin bases de datos. Cuando el programa se cierra, los datos se pierden.

## Cómo ejecutarlo

Se necesita Java 17 o superior. Desde la carpeta del proyecto:

```bash
cd src
javac *.java
java Main
```

Después se escribe un comando por línea. Para terminar se escribe `salir`.

## Qué hice, paso a paso

Lo construí por partes, de lo más básico a lo más completo:

**1. Primero las clases del dominio.** Empecé por `Paciente`, que es la clase padre con lo que comparten todos: nombre, peso, edad, estado, factor de dosis, dosis acumulada y la jaula. Después hice `Perro`, `Gato` y `AveExotica`, que heredan de `Paciente` y solo agregan lo propio de cada uno (raza y vacuna, nivel de estrés, envergadura). Por último `Jaula`, con su número, tamaño, zona y ocupante.

**2. Después el `Hospital`.** Es la clase que guarda los dos vectores y donde está toda la lógica. Cada comando es un método (`ingresarPerro`, `vacunar`, `asignar`, `medicar`, `alta`, `reporte`) que revisa las reglas en orden y devuelve un texto con el resultado (`OK: ...` o `ERROR: ...`). Así el hospital nunca imprime nada.

**3. Luego el `Main`.** Lee cada línea con `Scanner`, la separa por espacios, convierte los números y llama al método del hospital que corresponde. Lo que devuelve el hospital es lo que se imprime.

**4. Por último probé todo.** Fui probando cada comando con los casos normales y con los límites (peso exacto de 80 kg, edad 30, dosis justo en el máximo, etc.) para revisar que cada error saliera en el orden que pide el enunciado.

## Cómo está organizado

El código está en la carpeta `src/`, con un archivo `.java` por clase:

| Clase | Qué hace |
|---|---|
| `Paciente` | Clase padre (abstracta) con los datos comunes y el cálculo de la dosis máxima. |
| `Perro` | Hereda de `Paciente`. Tiene raza y si está vacunado. Factor de dosis 10. |
| `Gato` | Hereda de `Paciente`. Tiene nivel de estrés (0 a 10). Factor de dosis 6. |
| `AveExotica` | Hereda de `Paciente`. Tiene envergadura en cm. Factor de dosis 3. |
| `Jaula` | Número, tamaño (S, M, L), zona (A o B) y ocupante. Está libre si no tiene ocupante. |
| `Hospital` | Los vectores, el contador de pacientes y todas las reglas del negocio. |
| `Main` | La consola: lee comandos e imprime respuestas. Es la única clase que usa `Scanner` y `System.out`. |

Al arrancar, el método `cargarDatosIniciales()` crea las 6 jaulas, 1 (A, S), 2 (A, M), 3 (A, M), 4 (A, L), 5 (B, S) y 6 (B, M), y los tres pacientes: Rocky (perro, 30 kg), Misifu (gato, 4 kg) y Kiwi (ave, 0.8 kg).

## Cómo funciona cada comando por dentro

**Buscar un paciente.** Casi todos los comandos empiezan igual: recorro el vector desde 0 hasta el contador comparando el nombre con `equalsIgnoreCase`, así "rocky" y "Rocky" son el mismo. Si no lo encuentro devuelvo -1 y el comando responde `ERROR: paciente no encontrado`.

**`ingresar`.** Primero reviso que los datos sean válidos, luego que el nombre no esté repetido y al final que haya cupo. Si todo está bien, guardo el paciente en la posición del contador y le sumo 1. Un perro sin vacunar entra directo en `CUARENTENA`.

**`vacunar`.** Solo sirve para perros (lo reviso con `instanceof`). Marca al perro como vacunado y lo pasa de `CUARENTENA` a `INGRESADO`.

**`asignar`.** Calculo qué jaula necesita el paciente:
- Tamaño según el peso: menos de 5 kg es S, de 5 a 25 kg es M y más de 25 kg es L. Si es un ave con envergadura mayor a 60 cm, mínimo M.
- Zona: perros y gatos en la A, aves en la B.

Después recorro las jaulas en orden y uso la primera que esté libre y tenga ese tamaño y esa zona. Al asignar, la jaula guarda al paciente y el paciente guarda la jaula, así quedan enlazados.

**`medicar`.** La dosis máxima por aplicación es peso × factor de dosis (Rocky: 30 × 10 = 300 mg), y en total no puede pasar de 3 veces eso (Rocky: 900 mg). Si la dosis es válida, se suma al acumulado y el paciente pasa a `EN_TRATAMIENTO`. A los gatos el estrés les sube 2 cada vez, y con 8 o más ya no se pueden medicar.

**`alta`.** Solo funciona si el paciente está `EN_TRATAMIENTO`. Libero su jaula y lo saco del vector "compactándolo": cada paciente que estaba después se corre una posición hacia atrás, la última posición queda en `null` y el contador baja en 1. Así no quedan huecos en medio del vector.

**`reporte`.** Recorre los pacientes y muestra uno por línea con el peso a un decimal, y al final cuenta las jaulas libres:

```
--- REPORTE ---
Pacientes: 3/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Jaulas libres: 6
```

## Comandos

```
ingresar perro <nombre> <peso> <edad> <raza> <si/no>
ingresar gato <nombre> <peso> <edad>
ingresar ave <nombre> <peso> <edad> <envergadura>
vacunar <nombre>
asignar <nombre>
medicar <nombre> <mg>
alta <nombre>
reporte
salir
```

Ejemplo de uso:

```
ingresar perro Toby 12 3 criollo no
OK: Toby ingresado en CUARENTENA
asignar Toby
ERROR: paciente en cuarentena
vacunar Toby
OK: Toby vacunado
asignar Toby
OK: Toby asignado a jaula 2
medicar Toby 100
OK: Toby medicado con 100 mg
alta Toby
OK: Toby dado de alta
```

## Mensajes de error y qué significan

Cada comando revisa sus condiciones en un orden fijo. En cuanto una falla, se muestra ese error y el comando se cancela sin modificar nada.

### Generales

| Mensaje | Cuándo aparece | Ejemplo |
|---|---|---|
| `ERROR: comando desconocido` | La primera palabra no es un comando válido. | `bailar` |
| `ERROR: datos invalidos` | Faltan o sobran datos, o un número está mal escrito. | `vacunar` (sin nombre) |

### `ingresar` (en este orden: datos → duplicado → lleno)

| Mensaje | Cuándo aparece | Ejemplo |
|---|---|---|
| `ERROR: datos invalidos` | Nombre vacío, peso ≤ 0 o mayor al máximo (perro 80, gato 15, ave 15), edad fuera de 0 a 30, envergadura ≤ 0, o el perro no dice `si`/`no`. | `ingresar perro Max 90 3 pug si` |
| `ERROR: nombre duplicado` | Ya existe un paciente con ese nombre (no importan las mayúsculas). | `ingresar perro rocky 10 2 pug si` |
| `ERROR: hospital lleno` | Ya hay 10 pacientes. | — |

### `vacunar`

| Mensaje | Cuándo aparece |
|---|---|
| `ERROR: paciente no encontrado` | No hay ningún paciente con ese nombre. |
| `ERROR: solo aplica a perros` | El paciente es un gato o un ave. |
| `ERROR: ya vacunado` | El perro ya estaba vacunado. |

### `asignar`

| Mensaje | Cuándo aparece |
|---|---|
| `ERROR: paciente no encontrado` | No hay ningún paciente con ese nombre. |
| `ERROR: paciente en cuarentena` | Es un perro sin vacunar; primero hay que usar `vacunar`. |
| `ERROR: paciente ya tiene jaula` | El paciente ya tiene una jaula asignada. |
| `ERROR: no hay jaula compatible` | No queda una jaula libre del tamaño y la zona que necesita. |

### `medicar`

| Mensaje | Cuándo aparece |
|---|---|
| `ERROR: paciente no encontrado` | No hay ningún paciente con ese nombre. |
| `ERROR: datos invalidos` | Los mg son 0, negativos o no son un número. |
| `ERROR: paciente en cuarentena` | Es un perro sin vacunar. |
| `ERROR: paciente sin jaula` | Todavía no se le asignó jaula. |
| `ERROR: gato con estres alto` | Es un gato con estrés de 8 o más. |
| `ERROR: dosis excede el maximo` | Los mg superan peso × factor de dosis. |
| `ERROR: tope acumulado superado` | Con esta dosis el total pasaría de 3 veces la dosis máxima. |

### `alta`

| Mensaje | Cuándo aparece |
|---|---|
| `ERROR: paciente no encontrado` | No hay ningún paciente con ese nombre. |
| `ERROR: paciente no apto para alta` | El paciente no está `EN_TRATAMIENTO` (nunca fue medicado). |

## Conceptos de POO que usé

- **Clases y objetos:** cada paciente y cada jaula es un objeto, y `Hospital` los guarda en vectores.
- **Herencia:** `Perro`, `Gato` y `AveExotica` heredan de `Paciente` y llaman a `super(...)` pasando su factor de dosis, así no repito el código común. Como todos son `Paciente`, caben en el mismo vector `Paciente[]`.
- **Clase abstracta y polimorfismo:** `Paciente` es abstracta porque no existe un "paciente genérico". Cada subclase responde a su manera `getTipo()` y `getPesoMaximo()`, y el hospital las usa sin saber qué tipo de paciente es.
- **Encapsulamiento:** todos los atributos son `private`. Los setters revisan el valor y, si no es válido, no cambian nada (por ejemplo, `setNivelEstres(11)` no hace nada).
- **Separación de responsabilidades:** la lógica está en `Hospital` y la consola en `Main`. Si mañana se cambiara la consola por una ventana, el hospital quedaría igual.

## Decisiones que tomé

Algunas cosas el enunciado no las especificaba, así que las resolví así:

- **La jaula tiene que ser del tamaño exacto** que necesita el paciente (un gato de 4 kg va en una S, no en una M).
- **El estrés de un gato nuevo empieza en 0**, porque el comando `ingresar gato` no lo pide.
- **Datos mal escritos:** si a un comando le faltan o le sobran datos, o un número no se puede leer, sale `ERROR: datos invalidos`. Si la palabra del comando no existe, sale `ERROR: comando desconocido`.
- **Los mg se muestran sin `.0`** cuando son enteros (`100 mg`) y con decimales cuando los tienen (`12.5 mg`).
- **Margen al comparar dosis:** en Java los `double` no guardan exactos todos los decimales. Por ejemplo, `0.7 * 6` da `4.199999999999999` en vez de `4.2`, entonces a un gato de 0.7 kg no se le podía dar su dosis máxima de 4.2 mg. Para evitarlo, al comparar las dosis dejo un margen muy pequeño (`MARGEN = 0.0001`), que no cambia ningún resultado normal.
