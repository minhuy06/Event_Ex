package com.example.event_ex

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.event_ex.databinding.ActivityMainBinding

data class RegistrationInfo(
    val fullName: String,
    val studentId: String,
    val phone: String,
    val gender: String,
    val socialNetworks: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(studentId)
        parcel.writeString(phone)
        parcel.writeString(gender)
        parcel.writeString(socialNetworks)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RegistrationInfo> {
        override fun createFromParcel(parcel: Parcel): RegistrationInfo = RegistrationInfo(parcel)
        override fun newArray(size: Int): Array<RegistrationInfo?> = arrayOfNulls(size)
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentToast: Toast? = null

    companion object {
        const val EXTRA_INFO = "extra_registration_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEditTextEvents()
        setupRadioEvents()
        setupCheckBoxEvents()
        setupRegisterButton()
    }

    // 1. Event cho các EditText: Khi chạm vào hoặc gõ chữ đều hiện Toast thông báo
    private fun setupEditTextEvents() {
        bindEditTextToast(binding.edtFullName, "Họ và tên")
        bindEditTextToast(binding.edtStudentId, "Mã sinh viên")
        bindEditTextToast(binding.edtPhone, "Số điện thoại")
    }

    private fun bindEditTextToast(editText: EditText, label: String) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showInstantToast("Đang thao tác ô: $label")
            }
        }
        editText.doAfterTextChanged { text ->
            val value = text?.toString()?.trim().orEmpty()
            if (value.isNotEmpty()) {
                showInstantToast("Đang nhập $label: $value")
            }
            updateBottomStudentPreview()
        }
    }

    // 2. Event cho RadioButton Giới tính: Chọn sẽ hiện Toast thông báo
    private fun setupRadioEvents() {
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            radio?.let {
                showInstantToast("Đã chọn giới tính: ${it.text}")
                updateBottomStudentPreview()
            }
        }
    }

    // 3. Event cho các CheckBox Mạng xã hội: Chọn hoặc bỏ chọn sẽ hiện Toast thông báo
    private fun setupCheckBoxEvents() {
        val checkBoxes = listOf(
            binding.cbFacebook,
            binding.cbZalo,
            binding.cbTiktok,
            binding.cbInstagram
        )
        checkBoxes.forEach { cb ->
            cb.setOnCheckedChangeListener { _, isChecked ->
                val action = if (isChecked) "Đã chọn" else "Bỏ chọn"
                showInstantToast("$action MXH: ${cb.text}")
                updateBottomStudentPreview()
            }
        }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val info = collectCurrentInfo()
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(EXTRA_INFO, info)
            }
            startActivity(intent)
        }
    }

    private fun collectCurrentInfo(): RegistrationInfo {
        val name = binding.edtFullName.text.toString().trim().ifEmpty { "Chưa nhập" }
        val studentId = binding.edtStudentId.text.toString().trim().ifEmpty { "Chưa nhập" }
        val phone = binding.edtPhone.text.toString().trim().ifEmpty { "Chưa nhập" }

        val checkedRadioId = binding.rgGender.checkedRadioButtonId
        val gender = if (checkedRadioId != -1) {
            findViewById<RadioButton>(checkedRadioId).text.toString()
        } else {
            "Chưa chọn"
        }

        val socials = listOf(
            binding.cbFacebook,
            binding.cbZalo,
            binding.cbTiktok,
            binding.cbInstagram
        ).filter { it.isChecked }
            .joinToString(", ") { it.text.toString() }
            .ifEmpty { "Chưa chọn" }

        return RegistrationInfo(name, studentId, phone, gender, socials)
    }

    private fun updateBottomStudentPreview() {
        val info = collectCurrentInfo()
        binding.tvStudentInfoBottom.text =
            "SV thực hiện: Võ Minh Huy - MSV: 2415053122117\n" +
                    "Đang nhập -> Tên: ${info.fullName} | MSV: ${info.studentId} | GT: ${info.gender} | MXH: ${info.socialNetworks}"
    }

    private fun showInstantToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}