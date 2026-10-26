package com.example.event_ex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.example.event_ex.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val info = IntentCompat.getParcelableExtra(
            intent,
            MainActivity.EXTRA_INFO,
            RegistrationInfo::class.java
        )

        info?.let {
            binding.tvDetailName.text = "Họ và tên: ${it.fullName}"
            binding.tvDetailStudentId.text = "Mã sinh viên: ${it.studentId}"
            binding.tvDetailPhone.text = "Số điện thoại: ${it.phone}"
            binding.tvDetailGender.text = "Giới tính: ${it.gender}"
            binding.tvDetailSocial.text = "Mạng xã hội: ${it.socialNetworks}"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}