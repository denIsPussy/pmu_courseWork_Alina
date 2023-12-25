const jsonServer = require('json-server');
const server = jsonServer.create();
const router = jsonServer.router('data.json'); // путь к вашему JSON файлу
const middlewares = jsonServer.defaults();
const uuid = require('uuid');

server.use(middlewares);
server.use(jsonServer.bodyParser);

server.get('/ordersByDate', (req, res) => {
    const userId = parseInt(req.query.userId);
    const startDate = new Date(req.query.start);
    const endDate = new Date(req.query.end);

    const orders = router.db.get('orders')
        .filter(order => order.userId == userId)
        .filter(order => {
            const orderDate = new Date(order.date);
            return orderDate >= startDate && orderDate <= endDate;
        })
        .value();
    
    const totalSum = orders.reduce((sum, order) => sum + order.sum, 0);
    const orderCount = orders.length;

    res.jsonp({ orders, orderCount, totalSum });
});

server.get('/ordersByUser', (req, res) => {
    const userId = parseInt(req.query._userId);
    const page = parseInt(req.query._page);
    const limit = parseInt(req.query._limit);

    let orders = router.db.get('orders')
        .filter(order => order.userId === userId);

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
    const bouquets = router.db.get('bouquets').value();
    const orderBouquetCrossRefs = router.db.get('orderBouquetCrossRefs').value();

    const bouquetCounts = orderBouquetCrossRefs.reduce((acc, cur) => {
        acc[cur.bouquetId] = (acc[cur.bouquetId] || 0) + cur.count;
        return acc;
    }, {});

    const sortedBouquetIds = Object.entries(bouquetCounts)
        .sort((a, b) => b[1] - a[1])
        .map(entry => parseInt(entry[0]));

    const popularBouquets = bouquets
        .filter(bouquet => sortedBouquetIds.includes(bouquet.id))
        .sort((a, b) => sortedBouquetIds.indexOf(a.id) - sortedBouquetIds.indexOf(b.id));


    res.jsonp(popularBouquets);
});

server.use(router);
server.listen(8079, () => {
    console.log('JSON Server is running');
});
