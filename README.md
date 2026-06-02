# Kollama

Мобильное приложение для Android. Приложение выступает в роли интеллектуального графического терминала для взаимодействия с локальными большими языковыми моделями (LLM) через REST API платформы **Ollama** внутри локальной сети (LAN).

## 📌 Архитектурная концепция (Гибридный LAN-подход)

Система построена по клиент-серверной схеме:
1. **Вычислительный сервер (LAN Server):** ПК в локальной сети с Ollama, выполняющий тяжелые вычисления и инференс моделей без использования глобального интернета
2. **Мобильный клиент (Android Client):** Приложение для реактивного рендеринга чата, управления сетевыми конфигурациями и локального кэширования истории

## 🛠️ Технологический стек (Tech Stack)

* **IDE & Build System:** Android Studio, Gradle (Kotlin DSL)
* **Язык разработки:** Kotlin
* **Архитектурный шаблон:** **Clean Architecture** совместно с паттерном **MVI (Model-View-Intent)**
* **Пользовательский интерфейс (UI):** Jetpack Compose
* **Сетевой уровень:** Ktor HTTP Client
* **База данных:**  Room Локально на телефоне
* **Внедрение зависимостей:** Koin

## ⚙️ Функциональные возможности

* **Динамическая настройка связи:** Ручной ввод IP-Адреса сервера Ollama в локальной сети Wi-Fi через графический диалог настроек
* **Реактивный стриминг ответов:** Построчный вывод текста от нейросети в реальном времени с автоматическим плавным скроллом ленты сообщений 
* **Локальный кэш:** Полное сохранение истории диалогов, чатов и сообщений в локальной БД (Room)
* **Управление локальным хранилищем:** Возможность удаления сообщений/чатов из локальной БД
* **Динамический Выбор модели:** Автоматический пинг сервера по REST API (`GET /api/tags`) и вывод всех доступных на сервере LLM-моделей в выпадающем меню приложения

## 🗂️ Структура проекта


```text
app/src/main/java/com/kollama/app/
├── data
│   ├── local
│   │   ├── ChatDao.kt            # SQL-запросы Room к локальной БД SQLite
│   │   ├── ChatDatabase.kt       # Конфигурация локальной базы данных Room
│   │   ├── ChatEntity.kt         # Сущность таблицы чатов
│   │   ├── MessageEntity.kt      # Сущность таблицы сообщений
│   │   └── SettingsManager.kt    # Хранение настроек
│   ├── mapper
│   │   └── MessageMapper.kt      # Конвертация Entities/DTO в чистые Domain-модели
│   ├── remote
│   │   ├── dto
│   │   │   └── ChatDto.kt        # JSON-модели запросов и ответов API Ollama
│   │   └── KtorClient.kt         # HTTP-клиент Ktor (Chunked Transfer)
│   └── repository
│       └── ChatRepositoryImpl.kt # Логика стриминга и скрещивания сети с Room DB
├── di                            # dependency injection (Koin модули)
│   ├── ChatsModule.kt
│   ├── DatabaseModule.kt
│   ├── MessagesModule.kt
│   ├── NetworkModule.kt
│   ├── PresentationModule.kt
│   └── RepositoryModule.kt
├── domain                        # СЛОЙ БИЗНЕС-ЛОГИКИ
│   ├── model
│   │   ├── Chat.kt               # Модель чата
│   │   ├── ChatMessage.kt        # Модель сообщения
│   │   └── MessageRole.kt        # Определение ролей в диалоге (пользователь/ИИ)
│   ├── repository
│   │   ├── ChatRepository.kt     # Интерфейс чата
│   │   └── MessageRepository.kt  # Интерфейс сообщения
│   └── usecase                   # Сценарии использования (Use Cases)
│       ├── chats
│       │   ├── ChatsUseCase.kt
│       │   ├── CreateChatUseCase.kt
│       │   ├── DeleteChatUseCase.kt
│       │   ├── GetAllChatsUseCase.kt
│       │   └── UpdateChatNameUseCase.kt
│       └── messages
│           ├── ClearHistoryUseCase.kt
│           ├── DeleteMessageUseCase.kt
│           ├── GetChatHistoryUseCase.kt
│           ├── GetModelsUseCase.kt
│           ├── MessagesUseCase.kt
│           ├── RegenerateResponseUseCase.kt
│           └── SendMessageUseCase.kt
├── KollamaApp.kt                 # Инициализация контейнера Koin
├── presentation                  # СЛОЙ ГРАФИЧЕСКОГО ИНТЕРФЕЙСА (UI)
│   ├── chat                      # Паттерн управления состоянием MVI
│   │   ├── ChatContract.kt       # Определение однонаправленного потока (State, Event)
│   │   ├── ChatScreen.kt         # Главный экран приложения
│   │   └── ChatViewModel.kt      # Бизнес-контейнер состояния экрана
│   ├── components                # UI-компоненты Jetpack Compose
│   │   ├── ChatBubble.kt         # Отрисовка сообщений
│   │   ├── ChatDrawerItem.kt     # Элемент списка чатов в боковой шторке
│   │   ├── ChatInputBar.kt       # Панель ввода с кнопкой выбора LLM-модели (Робот)
│   │   ├── ChatList.kt           # Лента сообщений диалога с автоскроллом
│   │   ├── ChatTopBar.kt         # Верхняя панель с индикатором статуса подключения
│   │   ├── SettingsDialog.kt     # Диалог ручной конфигурации IP-адреса сервера
│   │   └── StatusInfoDialog.kt   # Окно информации о сервере
│   ├── MainActivity.kt           # Главная Activity приложения
│   └── theme                     # Дизайн Jetpack Compose (Цвета, Шрифты, Темы)
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── utils
    └── Constants.kt              # Константы при первом запуске
21 directories, 48 files
```

## Запуск

Для тестирования полной автономности системы без интернета:
1. Запустить Ollama с установленными моделями на ПК и убедиться, что сервер доступен по сети (переменная окружения `OLLAMA_HOST=0.0.0.0`).
2. Объединить ПК и Android-устройство в одну сеть.
3. Ввести IP-адрес сервера и сохранить настройки.


