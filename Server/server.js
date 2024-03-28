const express = require('express')
const app = express()
const mongoose = require('mongoose')
const productRouter = require('./routes/product')
const connectDB = async () => {
    try {
        await mongoose.connect('mongodb+srv://anhntph37315:Vu8sDvetRVBWVh1D@asm.7s13zgb.mongodb.net/ASM')
        console.log('Kết nối với database thành công');
    } catch (error) {
        console.error('Lỗi: ',error);
        process.exit(1)
    }
}
connectDB()
app.use(express.json())

app.use('/products',productRouter)
app.listen(3000,()=> console.log('Server đang chạy cổng 3000'))