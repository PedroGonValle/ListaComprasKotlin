# Lista de Compras Kotlin

A sua lista de compras pessoal para Android, feita em Kotlin!

**Seções:**
- [📱 Funcionalidades](#funcionalidades)
- [🏠 Tela Principal](#tela-principal)
- [⚙️ Tecnologias Utilizadas](#tecnologias-utilizadas)
- [🧱 Estrutura do Projeto](#estrutura-do-projeto)
- [🧠 Explicação do Código](#explicacao-do-codigo)
- [🧩 Main](#main)
- [📦 Dependências](#dependencias)

## Funcionalidades

    Adicionar itens: Digite o nome de um item e toque no botão para adicioná-lo à lista.

    Remover itens: Cada item possui um botão para remoção rápida.

    Persistência local: Os dados são salvos automaticamente no dispositivo usando Room Database, mesmo após fechar o app.

## Tela Principal
![image](https://github.com/user-attachments/assets/04a5a38f-25ce-47a7-abca-a9a01b42775f)

**Tela inicial do programa, sem nenhum item adicionado**

![image](https://github.com/user-attachments/assets/9ea4b30d-5cf2-44dc-9672-37ff1f3dbc87)

**A mesma tela após a adição dos itens "arroz", "feijao" e "batata"**

![image](https://github.com/user-attachments/assets/a53502e8-7a0c-408f-aefe-29250cc7b948)

**A mesma tela, agora, após a remoção do item "batata"**

## Tecnologias Utilizadas
### Linguagem e Frameworks

* **Kotlin**: Linguagem principal do projeto.

* **ViewModel**: Gerencia o estado e os dados da interface, respeitando o ciclo de vida.

* **LiveData**: Observa mudanças nos dados e atualiza a interface automaticamente.

* **RecyclerView**: Exibe listas de forma eficiente, usando o padrão ViewHolder.

* **View Binding**: Facilita o acesso seguro e direto às views do layout.

* **SQLite**: Banco de dados relacional embarcado utilizado para persistência local.

* **ContentValues**: Classe para estruturar operações de banco de dados.

### Interface do Usuário

* **CardView**: Componentes visuais com elevação e estilo moderno.

* **Ícones**: Uso de ícones, como o ícone de lixeira (baseline_delete_24.xml) para ações de remoção.

### Ferramentas de Build

* **Gradle**: Ferramenta para gerenciamento de dependências e build do projeto.

### Componentes Principais

* **Room Database**: Camada de abstração sobre o SQLite que facilita o uso do banco local, oferecendo uma API mais simples e segura.
    
* **LiveData**: Componente que permite observar dados de forma reativa, notificando a interface automaticamente quando há mudanças.
    
* **RecyclerView**:Componente eficiente para exibição de listas, que reutiliza views para otimizar desempenho.
    
* **ViewModel**: Classe que armazena e gerencia dados relacionados à UI, sobrevivendo a mudanças de configuração como rotações de tela.
    
* **Coroutines**: Recurso do Kotlin para execução de tarefas assíncronas, usado para operações no banco de dados sem travar a interface.

## Estrutura do Projeto
**O projeto segue a arquitetura MVVM, que garante:**

    Separação clara entre interface, lógica de negócios e dados.

    Código mais organizado e fácil de manter.

    Maior facilidade na criação de testes automatizados.
```
├── app/
│ ├── build.gradle.kts
│ └── src/
│ └── main/
│ ├── java/
│ │ └── pedrogonvalle/
│ │ └── com/
│ │ └── github/
│ │ └── listacompraskotlin/
│ │ ├── MainActivity.kt
│ │ ├── data/
│ │ │ ├── ItemDao.kt
│ │ │ └── ItemDatabase.kt
│ │ ├── model/
│ │ │ └── ItemModel.kt
│ │ └── viewmodel/
│ │ ├── ItemsAdapter.kt
│ │ ├── ItemsViewModel.kt
│ │ └── ItemsViewModelFactory.kt
│ └── res/
│ └── layout/
│ ├── activity_main.xml
│ └── item_layout.xml
│ └── drawable/
│ └── baseline_delete_24.xml
├── build.gradle.kts 
└── gradle.properties 
```
* View: Inclui a interface gráfica do app, composta por `MainActivity.kt` e os arquivos de layout XML (`activity_main.xml` e `item_layout.xml`).

* Data: Agrupa as classes relacionadas ao armazenamento local com Room, como `ItemDao.kt` (interface) e `ItemDatabase.kt` (classe).

* Model: Contém a classe `ItemModel.kt`, responsável por representar os dados de cada item da lista.

* ViewModel: Atua como intermediário entre a View e os dados (Data), centralizando a lógica de negócios e o controle da interface, é composta pelas classes `ItemsAdapter.kt`, `ItemsViewModel.kt` e `ItemsViewModelFactory.kt`.

## Explicação do Código

### Data
* **ItemDao.kt**: Esta interface representa o DAO responsável pela comunicação com o banco de dados Room no app. Através dela, é possível realizar operações básicas de persistência envolvendo o `ItemModel.kt`.
```
@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemModel")
    fun getAll(): LiveData<List<ItemModel>>

    @Insert
    fun insert(item: ItemModel)

    @Delete
    fun delete(item: ItemModel)
}
```
  * Ela é anotada com `@Dao`, o que permite ao Room gerar automaticamente o código necessário para acesso ao banco de dados.
    * O método `getAll()` retorna todos os itens armazenados, encapsulados em um LiveData, o que possibilita que a interface do usuário observe mudanças automaticamente.
    * O método `insert(item: ItemModel)` insere um novo item na base de dados.
    * O método delete(item: ItemModel) remove o item informado da base.
  * Esses métodos permitem manipular os dados de forma simples e segura, aproveitando os recursos de reatividade e persistência oferecidos pelo Room.

* **ItemDatabase.kt**: A classe `ItemDatabase.kt` representa a base de dados principal da aplicação e estende `RoomDatabase`, fornecendo acesso às operações de persistência relacionadas à entidade `ItemModel.kt`.
```
@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}
```
* A anotação `@Database(entities = [ItemModel::class]`, version = 1) informa ao Room quais modelos de dados serão armazenados e qual é a versão atual da estrutura do banco.
* O método abstrato `itemDao()` expõe uma instância do `ItemDao.kt`, permitindo que outras camadas da aplicação, como os ViewModels, interajam com os dados persistidos.

### Model
* **ItemModel.kt**: Define a estrutura dos itens que compõem a lista de compras. É uma `data class` simples, anotada com `@Entity`, indicando ao Room que essa classe representa uma tabela no banco de dados.
```
@Entity
data class ItemModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)
```
* Cada item possui dois atributos principais: um identificador `id`, que é a chave primária e é gerado automaticamente pelo sistema, e o `name`, que armazena o nome do item da lista.

### ViewModel
* **ItemsAdapter.kt**: O `ItemsAdapter.kt` é um adaptador personalizado para a RecyclerView, responsável por exibir a lista de itens (`ItemModel.kt`) na interface. Ele transforma cada objeto de dados em uma visualização reutilizável, otimizando a performance da lista.
 ```
 class ItemsAdapter(private val onItemRemoved: (ItemModel) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private var items = listOf<ItemModel>()

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView = view.findViewById<TextView>(R.id.textViewItem)
        val button = view.findViewById<ImageButton>(R.id.imageButton)

        fun bind(item: ItemModel) {
            textView.text = item.name
            button.setOnClickListener {
                onItemRemoved(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun updateItems(newItems: List<ItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }
   ```
  * A classe interna `ItemViewHolder` define os elementos visuais que compõem cada item na lista (como o nome e o botão de exclusão) e encapsula a lógica para preenchê-los com os dados.
  * O método `onBindViewHolder` é chamado para associar os dados de um `ItemModel.kt` à visualização correspondente.
  * O parâmetro `onItemRemoved` é uma função de callback passada ao adaptador, permitindo notificar quando o usuário clica no botão de remover.

* **ItemsViewModel.kt**: A classe `ItemsViewModel.kt` funciona como o elo entre a interface do usuário e a camada de dados da aplicação. Utiliza o padrão MVVM (Model-View-ViewModel) para manter o estado da UI e gerenciar a lógica de negócios de forma desacoplada e resiliente a mudanças de configuração, como rotações de tela.
```
class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val itemDao: ItemDao

    val itemsLiveData: LiveData<List<ItemModel>>

    init {
        val database = Room.databaseBuilder(
            getApplication(),
            ItemDatabase::class.java,
            "items_database"
        ).build()

        itemDao = database.itemDao()

        itemsLiveData = itemDao.getAll()
    }

    fun addItem(item: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = ItemModel(name = item)
            itemDao.insert(newItem)
        }
    }

    fun removeItem(item: ItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }
}
```
* A propriedade `itemDao` fornece acesso ao banco de dados via Room.
* A lista de itens é exposta como `LiveData` através de `itemsLiveData`, permitindo que a interface observe atualizações automaticamente.
* O método `addItem()` insere um novo item na base de dados de forma assíncrona usando `viewModelScope.launch`, evitando bloqueio da thread principal.
* O método `removeItem()` também executa a exclusão de itens dentro de uma coroutine.

* **ItemsViewModelFactory.kt**: Quando um ViewModel exige parâmetros personalizados no construtor — como o `Application`, no caso de `ItemsViewModel.kt` — o Android exige o uso de uma `ViewModelProvider.Factory` para criar a instância corretamente.
```
class ItemsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

### View:
* **activity_main.xml**: Este arquivo define a interface principal da aplicação — o ponto central de interação do usuário. O layout é simples e funcional, permitindo ao usuário visualizar e gerenciar sua lista de compras de forma direta.
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="pedrogonvalle.com.github.listacompraskotlin.MainActivity">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:layout_marginBottom="16dp"
        android:background="?attr/colorPrimary"
        android:elevation="4dp"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

    <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:hint="Nome do produto"
        android:inputType="text"/>

    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:text="inserir" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />
</LinearLayout>
```
* Uma `Toolbar` no topo, que exibe o título da tela.
* Um campo de texto (`EditText`) onde o usuário pode digitar o nome de um novo item.
* Um botão (`Button`) que adiciona o item à lista quando clicado.
* Uma `RecyclerView` que mostra todos os itens já adicionados, atualizando automaticamente quando novos dados são inseridos ou removidos.
* Este layout está conectado ao ViewModel e ao Adapter, formando o elo visual entre o banco de dados e o usuário.

* **item_layout.xml**: O `item_layout.xml` descreve como cada item individual será visualmente representado dentro da RecyclerView. Esse layout é inflado pelo `ItemsAdapter.kt` para exibir cada elemento da lista de forma consistente.
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="40dp"
    android:gravity="center_vertical">

    <TextView
        android:id="@+id/textViewItem"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:layout_weight="1"
        android:textSize="18sp"
        android:textStyle="bold"
        tools:text="Novo Item" />

    <ImageButton
        android:id="@+id/imageButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:src="@drawable/baseline_delete_24"
        android:layout_marginEnd="16dp"/>

</LinearLayout>
```
* Um `TextView`, responsável por exibir o nome do item da lista.
* Um `ImageButton`, que permite ao usuário remover o item com um simples toque.

# Main
* **MainActivity.kt**: A `MainActivity.kt` é a tela principal da aplicação e funciona como o ponto de entrada da interface do usuário. Ela estende AppCompatActivity para aproveitar os componentes modernos de UI do Android.
```
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Lista de Compras"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val itemsAdapter = ItemsAdapter { item ->
            viewModel.removeItem(item)
        }
        recyclerView.adapter = itemsAdapter

        val button = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.editText)

        button.setOnClickListener {
            if (editText.text.isEmpty()) {
                editText.error = "Preencha um valor"
                return@setOnClickListener
            }

            viewModel.addItem(editText.text.toString())
            editText.text.clear()
        }

        val viewModelFactory = ItemsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ItemsViewModel::class.java)

        viewModel.itemsLiveData.observe(this) { items ->
            itemsAdapter.updateItems(items)
        }
    }
}
```
* O layout da tela é definido com `setContentView(R.layout.activity_main)`.
* A `Toolbar` é configurada para exibir o título da aplicação.
* O `ItemsViewModel.kt` é instanciado usando a `ItemsViewModelFactory.kt`, que fornece o contexto necessário.
* A `RecyclerView` é configurada com um `LinearLayoutManager` e o `ItemsAdapter.kt`, que exibe a lista de itens e trata a remoção de cada um.
* A lista de itens no ViewModel é observada usando `LiveData`. Sempre que houver alteração, o adapter é atualizado automaticamente.
* Um botão de adicionar permite inserir novos itens na lista: ele lê o conteúdo de um campo de texto (`EditText`), envia para o ViewModel e, em seguida, limpa o campo.

# Dependências
**As seguintes bibliotecas foram utilizadas:**
* kapt (plugin): Permite que projetos Kotlin utilizem processadores de anotações Java, como os usados pelo Room. Ele é essencial para que o Room gere automaticamente o código necessário para o banco de dados a partir das anotações (`@Entity`, `@Dao`, `@Database` etc.).
* LiveData: Permite que a Activity observe os dados do banco expostos pelo ViewModel de forma reativa e segura para o ciclo de vida.
* RecyclerView: Utilizada para exibir a lista de itens. Garante alta performance ao reutilizar as views existentes ao invés de criar novas para cada item.
* AppCompat: Fornece componentes modernos de UI com compatibilidade retroativa.
* Activity KTX: Adiciona extensões Kotlin que tornam o código da Activity mais idiomático e conciso.
* Room Runtime: É o núcleo do Room, fornecendo APIs para persistência de dados no SQLite.
* Room KTX: Oferece suporte nativo a Coroutines e outras extensões Kotlin no Room.
* Room Compiler: Processa anotações do Room e gera automaticamente os DAOs e estruturas do banco. Usado via kapt.
* Kotlin Coroutines: Suporte para código assíncrono com coroutines no Android, tornando operações como inserção e remoção no banco não bloqueantes e mais legíveis.
