package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.AdminAnswerRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryPageRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryUpdateRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.repository.InquiryRepository;
import com.example.echo.domain.inquiry.service.InquiryService;
import com.example.echo.global.api.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryRepository inquiryRepository;

    // USER 회원 1:1 문의 등록
    @PreAuthorize("authentication.principal.memberId == #InquiryRequestDTO.memberId")
    @PostMapping
    public ResponseEntity<InquiryResponseDTO> registerInquiry(@RequestBody InquiryRequestDTO inquiryRequestDTO) {
        InquiryResponseDTO registeredInquiry = inquiryService.createInquiry(inquiryRequestDTO);
        return ResponseEntity.ok(registeredInquiry);
    }

    // 모든 회원 1:1 문의 단건 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDTO> getInquiry(@PathVariable Long inquiryId) {
        InquiryResponseDTO foundInquiry = inquiryService.getInquiryById(inquiryId);
        return ResponseEntity.ok(foundInquiry);
    }

    // ADMIN/USER 회원 종류에 따른 모든 1:1 문의 전체 리스트 조회
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDTO>> getAllInquiries(
            @RequestParam Long memberId,    // 임시 url 부여. 로그인 기능 추가 시 Authentication으로 대체
            @ModelAttribute InquiryPageRequestDTO inquiryPageRequestDTO) {
        Page<InquiryResponseDTO> inquiriesPage = inquiryService.getInquiriesByMemberRole(memberId, inquiryPageRequestDTO);
        return ResponseEntity.ok(inquiriesPage);
    }

    //1:1문의 수정
    @PreAuthorize("authentication.principal.memberId == #InquiryRequestDTO.memberId")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponseDTO>> updateInquiry(@PathVariable Long inquiryId, @RequestBody InquiryRequestDTO inquiryRequestDTO) {
        InquiryResponseDTO updatedInquiry = inquiryService.updateInquiry(inquiryId, InquiryUpdateRequestDTO.builder().build());
        return ResponseEntity.ok(ApiResponse.success(updatedInquiry));
    }

    // 관리자와 회원이 1:1 문의 삭제
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.memberId == #InquiryRequestDTO.memberId")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(@PathVariable Long inquiryId){
        inquiryService.deleteInquiry(inquiryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 관리자 답변
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<ApiResponse<Void>> addAnswer(@PathVariable Long inquiryId, @RequestBody AdminAnswerRequestDTO adminAnswerRequestDTO) {
        inquiryService.addAnswer(inquiryId, adminAnswerRequestDTO.getReplyContent());
        return ResponseEntity.ok(ApiResponse.success(null));
}





}

