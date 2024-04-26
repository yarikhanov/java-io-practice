
Необходимо реализовать консольное CRUD приложение, которое имеет следующие сущности:

Writer (id, firstName, lastName, List<Post> posts)
Post (id, title, content, List<Label> labels)
Label(id, name)
Status (enum ACTIVE, DELETED)

Каждая сущность имеет поле Status. В момент удаления, мы не удаляем запись из файла, а меняем её статус на DELETED.

В качестве хранилища данных необходимо использовать текстовые файлы:
writers.json, posts.json, labels.json

Пользователь в консоли должен иметь возможность создания, получения, редактирования и удаления данных.

Слои:
model - POJO клаcсы
repository - классы, реализующие доступ к текстовым файлам
controller - обработка запросов от пользователя
view - все данные, необходимые для работы с консолью



Например: Writer, WriterRepository, WriterController, WriterView и т.д.


Для репозиторного слоя желательно использовать базовый интерфейс:
interface GenericRepository<T,ID>

interface WriterRepository extends GenericRepository<Writer, Long>

class GsonWriterRepositoryImpl implements WriterRepository

Для работы с json необходимо использовать библиотеку Gson(https://mvnrepository.com/artifact/com.google.code.gson/gson)
Для импорта зависимостей - Maven/Gradle на выбор.
