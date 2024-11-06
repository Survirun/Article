import gspread
from oauth2client.service_account import ServiceAccountCredentials
import json

# Google Sheets API 접근을 위한 범위 설정
scope = ["https://spreadsheets.google.com/feeds", "https://www.googleapis.com/auth/spreadsheets"]

# 서비스 계정 인증
creds = ServiceAccountCredentials.from_json_keyfile_name('app/script/authority.json', scope)
client = gspread.authorize(creds)

# 구글 시트 열기 (시트의 URL에서 'd/'와 '/edit' 사이의 부분 사용)
sheet = client.open_by_key('1G6Z5EEl-dHRDEd8W2SJ4ryiYBPlnZc0YtPPRlmvKqd8').sheet1

# 모든 데이터 가져오기
data = sheet.get_all_records()

# JSON 변환을 위한 기본 구조 초기화
result = [{"days": []}]  # 리스트 안에 딕셔너리 형태로 "days" 키 설정

# 각 문제에 대한 데이터를 JSON 형식으로 변환
current_day = None
current_day_data = None

for row in data:
    if current_day != row['Day']:
        if current_day_data is not None:
            result[0]["days"].append(current_day_data)  # "days" 리스트에 추가

        current_day = row['Day']
        current_day_data = {
            "day": current_day,
            "title": row['Title'].strip(),  # 공백 제거
            "questions": []
        }

    question_data = {
        "id": row['ID'],
        "category": row['Category'],
        "question": row['Question'],
        "options": [row['Option 1'], row['Option 2'], row['Option 3'], row['Option 4']],
        "answer": row['Answer'],
        "explanation": row['Explanation']
    }
    current_day_data['questions'].append(question_data)

# 마지막 날 데이터 추가
if current_day_data:
    result[0]["days"].append(current_day_data)

# 최종 JSON 구조
final_json = result

# JSON 형식으로 출력
json_output = json.dumps(final_json, ensure_ascii=False, indent=2)
print(json_output)
with open('app/src/main/assets/output.json', 'w', encoding='utf-8') as json_file:
    json.dump(final_json, json_file, ensure_ascii=False, indent=2)