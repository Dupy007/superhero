package org.example

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


var BASE_URL = "https://www.superheroapi.com/api.php/10224979642372552/search/";

fun main() {
    try {
        print("Entrez le nom du premier super-héros : ")
        var hero1 = readLine()
        print("Entrez le nom du deuxième super-héros : ")
        var hero2 = readLine()
        var data1 = RequestUtils.getData(hero1.toString())
        var data2 = RequestUtils.getData(hero2.toString())
        var intelligence1 = 0
        var intelligence2 = 0
        if (data1.response.contains("success")){
            intelligence1 = data1.results.get(0).powerstats.intelligence.toIntOrNull()!!
        }
        if (data2.response.contains("success")){
            intelligence2 = data2.results.get(0).powerstats.intelligence.toIntOrNull()!!
        }
        if (intelligence1 != 0 && intelligence2 != 0) {
            println("$hero1 -> ($intelligence1) - $hero2 -> ($intelligence2).")
            if (intelligence1 > intelligence2) {
                println("$hero1 est plus intelligent(e) que $hero2.")
            } else if (intelligence1 < intelligence2) {
                println("$hero2 est plus intelligent(e) que $hero1.")
            } else {
                println("$hero1 et $hero2 ont la même intelligence.")
            }
        } else if (intelligence1 != 0) {
            println("Impossible de déterminer l'intelligence de $hero2. $hero1 a une intelligence de $intelligence1.")
        } else if (intelligence2 != 0) {
            println("Impossible de déterminer l'intelligence de $hero1. $hero2 a une intelligence de $intelligence2.")
        } else {
            println("Impossible de déterminer l'intelligence des deux super-héros.")
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }


}

object RequestUtils {
    val client = OkHttpClient();
    val gson = Gson();
    fun getData(name: String): Result {
        val formattedName = name.replace(" ", "%20")
        var json = sendGet(BASE_URL + formattedName)
        return gson.fromJson(json, Result::class.java)
    }

    fun sendGet(url: String): String? {
        println("url : " + url)
        val req = Request.Builder().url(url).build()
        var responses: Response? = null
        responses = client.newCall(req).execute();

        if (responses.code == 200) {
            // Get response
            return responses.body?.string();
        }
        throw Exception(responses.toString())
    }
}

data class Result(
    val response: String,
    val results: List<ResultX>,
)

data class ResultX(
    val name: String,
    val powerstats: Powerstats,
)


data class Powerstats(
    val intelligence: String,
)

