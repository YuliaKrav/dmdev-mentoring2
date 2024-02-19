# Название проекта: Интернет-магазин

## Описание

Краткое описание интернет-магазина.

## Сущности системы

### Пользователь (User)

- Имя (First Name)
- Фамилия (Last Name)
- Почтовый ящик (Email)
- Пароль (Password)
- Роль (Role)

### Товар (Product)

- Название (Name)
- SKU (Stock Keeping Unit) - Уникальный идентификатор товара.
- Краткое описание (Short Description)
- Полное описание (Full Description)
- Цена (Price)
- URL изображения (Image URL)
- Количество на складе (Units in Stock)
- Дата создания (Date Created)
- Последнее обновление (Last Updated)
- Категория (Category)
- Бренд (Brand)
- Страна происхождения (Country of Origin)

### Заказ (Order)

- Номер отслеживания (Order Tracking Number)
- Товары в заказе (Items in Order) - Связь с сущностью OrderItem.
- Общая цена (Total Price)
- Общее количество (Total Quantity)
- Статус (Status)
- Дата создания (Date Created)
- Последнее обновление (Last Updated)
- Адрес доставки (Shipping Address)
- Адрес выставления счета (Billing Address)
- Валюта (Currency)

### Адрес (Address)

- Улица (Street)
- Город (City)
- Штат/Провинция (State/Province)
- Страна (Country)
- Почтовый индекс (Zip Code)

### Аналоги товаров (Product_Analogs)

- Продукт (Product) - Ссылка на основной продукт.
- Аналог Продукта (Analog Product) - Ссылка на аналогичный продукт, который может служить заменой основному.

## Ограничения и действия в системе

### Ограничения
1. Пользователь не сможет оформить заказ, если на складе недостаточно товаров выбранного типа.
2. Пользователь может выбрать аналог товара, если искомый товар отсутствует на складе.

### Действия в системе
1. Добавить в систему нового пользователя. 
2. Добавить в систему новый товар (доступно администраторам). 
3. Оформить заказ для выбранного пользователя. Пользователи могут добавлять товары в корзину и оформлять заказы, указывая адрес 
доставки и платежные данные.

4. Просмотреть список всех пользователей системы (доступно администраторам).
5. Просмотреть список всех заказов выбранного пользователя. Пользователи могут видеть историю своих заказов.
6. Просмотреть список всех товаров в системе. Пользователи могут просматривать доступные товары, искать по категориям, брендам или ключевым словам.
7. Просмотреть список всех заказов, в которые включен выбранный товар (администраторы могут отслеживать популярность товаров через заказы). 
8. Удалить заказы, сделанные раньше введённой даты. 


## Технологии

Hibernate, PostgreSQL, Maven.