import java.awt.color.ColorSpace
import java.awt.color.ICC_ColorSpace
import java.awt.color.ICC_Profile
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

const val inputDirPath = "F:/photo/art_and_culture_1"
const val outputDirPath = "F:/photo/art_and_culture_1/ready_to_print"
val rgbColorSpace: ColorSpace? = ColorSpace.getInstance(ColorSpace.CS_sRGB)
val cmkProfile: ICC_Profile? = ICC_Profile.getInstance(FileInputStream("src/main/resources/eciCMYK_v2.icc"))
val cmykColorSpace: ICC_ColorSpace = ICC_ColorSpace(cmkProfile)

fun main() {
    val dir = File(inputDirPath)
    val files = dir.listFiles()

    files?.forEach { item ->
        if(item.isFile) {
            convertRGBToCMYK(item.nameWithoutExtension)
        }
    }

}

fun convertRGBToCMYK(name: String) {
    val imagePath = File("$inputDirPath/${name}.jpg")
    val rgbImg: BufferedImage = ImageIO.read(imagePath)

    val cmykImage: BufferedImage = BufferedImage(
        rgbImg.width,
        rgbImg.height,
        BufferedImage.TYPE_INT_RGB
    )

    val colorCovertOp = ColorConvertOp(rgbColorSpace, cmykColorSpace, null)

    colorCovertOp.filter(rgbImg, cmykImage)

    val outputImage = File("${outputDirPath}/${name}.tif")
    ImageIO.write(cmykImage, "TIFF", outputImage)
    println("$name is converted and saved at ${outputImage.path}")
}