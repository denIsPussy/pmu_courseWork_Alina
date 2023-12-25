const jsonServer = require('json-server');
const server = jsonServer.create();
const router = jsonServer.router('data.json'); // путь к вашему JSON файлу
const middlewares = jsonServer.defaults();

server.use(middlewares);
server.use(jsonServer.bodyParser);

// Фильтрация заказов по диапазону дат
server.get('/ordersByDate', (req, res) => {
    const userId = parseInt(req.query.userId);
    const startDate = new Date(req.query.start);
    const endDate = new Date(req.query.end);

    // Фильтруем заказы по этим ID и датам
    const orders = router.db.get('orders')
        .filter(order => order.userId == userId)
        .filter(order => {
            const orderDate = new Date(order.date);
            return orderDate >= startDate && orderDate <= endDate;
        })
        .value();
    console.log(orders)
    const totalSum = orders.reduce((sum, order) => sum + order.sum, 0);
    const orderCount = orders.length;

    res.jsonp({ orderCount, totalSum });
});

server.get('/ordersByUser', (req, res) => {
    const userId = parseInt(req.query._userId);
    const page = parseInt(req.query._page);
    const limit = parseInt(req.query._limit);

    let orders = router.db.get('orders')
        .filter(order => order.userId === userId);

    // Если заданы параметры пагинации, применяем их
    if (!isNaN(page) && !isNaN(limit)) {
        const startIndex = (page - 1) * limit;
        const endIndex = page * limit;
        orders = orders.slice(startIndex, endIndex);
    }

    orders = orders.value();
    console.log(orders);

    res.jsonp(orders);
});



server.get('/popularBouquets', (req, res) => {
    // Получаем данные из JSON
    const bouquets = router.db.get('bouquets').value();
    const orderBouquetCrossRefs = router.db.get('orderBouquetCrossRefs').value();

    // Считаем общее количество каждого букета в заказах
    const bouquetCounts = orderBouquetCrossRefs.reduce((acc, cur) => {
        acc[cur.bouquetId] = (acc[cur.bouquetId] || 0) + cur.count;
        return acc;
    }, {});

    // Формируем массив ID букетов, отсортированных по популярности (по общему количеству в заказах)
    const sortedBouquetIds = Object.entries(bouquetCounts)
        .sort((a, b) => b[1] - a[1])
        .map(entry => parseInt(entry[0]));

    // Фильтруем исходный список букетов, чтобы оставить только популярные
    const popularBouquets = bouquets
        .filter(bouquet => sortedBouquetIds.includes(bouquet.id))
        .sort((a, b) => sortedBouquetIds.indexOf(a.id) - sortedBouquetIds.indexOf(b.id));


    // Возвращаем отсортированный список букетов
    res.jsonp(popularBouquets);
});


// Сортировка букетов по цене (встроенная функциональность JSON Server)
// Пример запроса: GET /bouquets?_sort=price&_order=asc

server.use(router);
server.listen(8079, () => {
    console.log('JSON Server is running');
});
