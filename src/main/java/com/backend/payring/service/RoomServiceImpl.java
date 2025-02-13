package com.backend.payring.service;

import com.backend.payring.code.ErrorCode;
import com.backend.payring.converter.RoomConverter;
import com.backend.payring.dto.room.DurationStatusDTO;
import com.backend.payring.dto.room.RoomDTO;
import com.backend.payring.dto.room.TeamMemberDTO;
import com.backend.payring.entity.RoomEntity;
import com.backend.payring.entity.TeamMemberEntity;
import com.backend.payring.entity.UserEntity;
import com.backend.payring.entity.enums.RoomStatus;
import com.backend.payring.exception.RoomException;
import com.backend.payring.exception.UserException;
import com.backend.payring.repository.RoomRepository;
import com.backend.payring.repository.TeamMemberRepository;
import com.backend.payring.repository.TransferRepository;
import com.backend.payring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public RoomDTO.DetailRes createRoom(Long userId, RoomDTO.CreateReq req) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        RoomEntity room = RoomConverter.toEntity(user, req);
        room = roomRepository.save(room);

        TeamMemberEntity teamMember = new TeamMemberEntity(
                null,
                null,
                true,
                true,
                user,
                room
        );

        teamMemberRepository.save(teamMember);

        return RoomConverter.toRes(room);
    }

    @Transactional
    public void deleteRoom(Long userId, Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        TeamMemberEntity owner = room.getTeamMembers().stream()
                .filter(TeamMemberEntity::getIsOwner)
                .findFirst()
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_OWNER_NOT_FOUND));

        if (!owner.getUser().getId().equals(userId)) {
            throw new RoomException(ErrorCode.NOT_ROOM_OWNER);
        }

        if (room.getRoomStatus() != RoomStatus.COLLECTING) {
            throw new RoomException(ErrorCode.CANNOT_DELETE_ROOM);
        }

        roomRepository.delete(room);
    }

    @Transactional
    public void inviteMember(Long userId, TeamMemberDTO.InviteReq req) {
        RoomEntity room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        TeamMemberEntity owner = room.getTeamMembers().stream()
                .filter(TeamMemberEntity::getIsOwner)
                .findFirst()
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_OWNER_NOT_FOUND));

        if (!owner.getUser().getId().equals(userId)) {
            throw new RoomException(ErrorCode.NOT_ROOM_OWNER);
        }

        UserEntity invitedUser = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        boolean alreadyInvited = room.getTeamMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(invitedUser.getId()));

        if (alreadyInvited) {
            throw new RoomException(ErrorCode.ALREADY_INVITED_MEMBER);
        }

        TeamMemberEntity teamMember = TeamMemberEntity.builder()
                .user(invitedUser)
                .room(room)
                .isOwner(false)
                .isJoin(false)
                .build();

        room.getTeamMembers().add(teamMember);
    }

    @Transactional
    public void acceptInvitation(Long userId, Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        TeamMemberEntity member = room.getTeamMembers().stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RoomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getIsJoin()) {
            throw new RoomException(ErrorCode.ALREADY_JOINED_MEMBER);
        }

        member.acceptInvitation();
    }

    @Transactional
    public void rejectInvitation(Long userId, Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        TeamMemberEntity member = room.getTeamMembers().stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RoomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getIsJoin()) {
            throw new RoomException(ErrorCode.ALREADY_JOINED_MEMBER);
        }

        room.getTeamMembers().remove(member);
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));

        if (room.getRoomStatus() != RoomStatus.COLLECTING) {
            throw new RoomException(ErrorCode.CANNOT_LEAVE_ROOM);
        }

        TeamMemberEntity member = room.getTeamMembers().stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RoomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getIsOwner()) {
            throw new RoomException(ErrorCode.OWNER_CANNOT_LEAVE);
        }

        room.getTeamMembers().remove(member);
    }

    @Override
    public List<RoomDTO.InvitedRoomList> getInvitedRooms(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return teamMemberRepository.findByUserIdAndIsJoinFalse(userId).stream()
                .map(RoomConverter::toInvitedRoomRes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DurationStatusDTO.DurationStatus getDurationStatus(Integer day, UserEntity user) {
        // 현재 날짜로부터 N일 전
        LocalDateTime startDate = LocalDateTime.now().minusDays(day);

        // 사용자가 속한 방 ID 목록
        List<Long> userRoomIds = teamMemberRepository.findRoomIdsByUserId(user.getId());

        // 해당 사용자가 속한 방 중 COLLECTING 상태가 아닌, 기간 내 생성된 방 조회
        List<RoomEntity> rooms = roomRepository.findByIdInAndCreatedAtAfterAndRoomStatusNot(
                userRoomIds, startDate, RoomStatus.COLLECTING
        );
        System.out.println(rooms);

        int totalSettledAmount = 0; // 총 정산 금액
        int unSettledAmount = 0; // 아직 정산하지 않은 금액

        List<DurationStatusDTO.RoomInfo> roomInfoList = new ArrayList<>();

        for (RoomEntity room : rooms) {

            // isComplete==true인 transfer 총합 계산 (완료된 송금 금액)
            int settledAmount = transferRepository.sumCompletedTransfersByRoomId(room.getId());

            // isComplete==false인 transfer 총합 계산 (미완료된 송금 금액)
            int unsettledAmount = transferRepository.sumUncompletedTransfersByRoomId(room.getId());

            totalSettledAmount += settledAmount; // 완료된 총 금액 더함
            unSettledAmount += unsettledAmount; // 아직 송금되지 않은 총 금액 더함

            roomInfoList.add(RoomConverter.toRoomInfo(room));
        }

        return RoomConverter.toDurationStatus(totalSettledAmount, unSettledAmount, roomInfoList);
    }



}