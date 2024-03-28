const express = require('express')
const router = express.Router()
const Product = require('../models/Product')


// @route POST /add
// @desc Creater product
// @access Public

router.post('/add', async (req, res) => {
    const { name, gia } = req.body

    if (!name || !gia)
        return res.status(400).json({ success: false, message: 'Không để trống tên và giá' })

    try {
        const newProduct = new Product({ name, gia })
        await newProduct.save()
        console.log('Thêm thành công');
        res.json(newProduct)
    } catch (error) {
        console.error('Lỗi: ', error);
        res.status(500).json({ success: false, message: 'Lỗi server' })
    }
})



// @route GET /
// @desc Creater product
// @access Public


router.get('/', async (req, res) => {
    try {
        const products = await Product.find()

        res.send(products)
        console.log('hiển thi dữ liệu thành công');
    } catch (error) {
        console.error('Lỗi server: ', error);
        res.status(500).json({ success: false, message: 'Lỗi server' })
    }
})



// @route PUT /update
// @desc Update product
// @access Public


router.put('/:id', async (req, res) => {
    const { name, gia } = req.body

    if (!name || !gia)
        return res.status(400).json({ success: false, message: 'Không để trống tên và giá' })
    try {
        let updatePro = { name, gia }
        updatePro = await Product.findOneAndUpdate({ _id: req.params.id }, updatePro, { new: true })
        if (!updatePro)
            return res.status(400).json({ success: false, message: 'Sai id' })
        res.json({success:true,message:'Sửa thành công',product: updatePro})
        console.log('Sửa thành công');
    } catch (error) {
        console.error('Lỗi: ', error);
        res.status(500).json({ success: false, message: 'Lỗi server' })
    }
})



// @route DELETE /delete
// @desc Delete product
// @access Public


router.delete('/:id', async (req, res) => {
    try {
        const deletePro = await Product.findOneAndDelete({ _id: req.params.id })
        if (!deletePro)
            return res.status(400).json({ success: false, message: 'Sai id' })
        res.json({success:true,message:'Xóa thành công'})
        console.log('Xóa thành công');
    } catch (error) {
        console.error('Lỗi: ', error);
        res.status(500).json({ success: false, message: 'Lỗi server' })
    }
})
module.exports = router