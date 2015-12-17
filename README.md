# BqDropboxApp

Proyecto desarrollado en Android Studio, usando la api de Dropbox.

El funcionamiento es el siguiente: al abrir la app por primera vez, nos pedirá que iniciemos sesión con nuestra cuenta de dropbox mediante un enlace. Al clicarlo, nos mandará a la app de dropbox o bien a una web en el navegador por defecto, en caso de no tener la app de dropbox instalada en el dispositivo. 
Una vez realizado el login, de vuelta a la app, entraremos en la pantalla principal, donde la app se conectará a la api de dropbox para obtener los archivos con extensión .epub. También tienes las opciones de seleccionar el tipo de vista (cuadrícula o lista), de ordenar por título o fecha, y de subir un archivo .epub a dropbox desde el almacenamiento del dispositivo. Además en la misma pantalla, en el menú de la izquierda, aparecen los datos del usuario y la opción de cerrar la sesión.
Al clicar en un ebook del listado nos iríamos a la pantalla de detalles, donde aparecerán la portada del libro, su título, su autor y el resumen de su contenido. También está la opción de abrir el archivo .epub desde cualquier app instalada en el dispositivo que sea capaz de leerlo.

## Estructura del proyecto

- app
  - adapter
    * EbupsAdapter
  - config
    * Constants
    * Preferences
  - model
    * EpubModel
  - util
    * Util
  * DetailsActivity
  * ListActivity
  * MainActivity
- network
  * DBApi
  * NetworkWueue

## Principales dificultades y decisiones tomadas

Para empezar, tenemos las clases Constants y Preferences, donde se definen las constantes a usar en el proyecto y la información a almacenar cuando se cierre y se vuelva a abrir la app.

Lo siguiente es crear la clase DBApi, un singleton con una instancia de la clase DropboxAPI, de forma que se puedan hacer peticiones a la api de drobox desde cualquier parte de la aplicación sin tener que crear una instancia cada vez. En el mismo paquete de esa clase se encuentra la clase NetworkQueue, que es otro singleton con una instancia de RequestQueue para hacer peticiones, aunque finalmente no ha sido necesario usarla dado que con las funciones que la clase DropboxAPI encapsula todas las llamadas.

Otra decisión es la definición del modelo de datos para la app. Dado que no estaba muy claro qué datos iba a necesitar de las respuestas de la api de dropbox, en primer lugar opté por usar como modelo la propia clase que devuelve la api, con toda la información. Finalmente, como también es necesario guardar la información de cada ebook, no sólo de los metadatos que llegan desde dropbox, también se incluye en el modelo la clase Book, que nos da la información del título, portada y demás de cada libro.

El listado de libros está realizado de la siguiente forma: en el layout tenemos un GridView, al cual tenemos conectado un adaptador de clase EpubsAdapter. el cual, en función de su propiedad mViewMode inflará el xml layout_item_list o layout_item_grid. El adaptador recibe una lista de metadatos devuelto por la api de dropbox, muestra el listado con una imagen por defecto para cada libro, con el nombre del fichero y la fecha de modificación, y lanza un AsyncTask que se descargue la información del ebook para que sea procesado con la librería EbookReader. En ese momento, si el elemento aún es visible, actualizará su vista con la imagen de portada del ebook y su título.

En esta misma pantalla se añade el menú lateral NavigationView, donde aparece el nombre del usuario y su correo, y la opción para cerrar la sesión del usuario actual. También está presente en esta pantalla el componente SwipeRefreshLayout, que nos permite refrescar el contenido del listado.

Al clicar en un libro, nos lo descargamos en el dispositivo, en el área interna de la app, pero con modo MODE_WORLD_READABLE, para que en la siguiente pantalla, donde se mostrará la información del libro descargado, podamos tener una opción de abrir el ebook con otra aplicación. En esta pantalla tendremos el componentes CollapsingToolbarLayout, que mostrará con efecto parallax la portada del libro junto con su título. Debajo, hacuendo uso de componentes CardView, mostramos por un lado portada, título y auto, y por otro la descripción del libro en un máximo de 5 líneas.
