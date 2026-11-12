# -*- coding: utf-8 -*-
"""
Academic Project Report Generator for Campus Peer-to-Peer Marketplace (CampusBazaar)
Subject: Object-Oriented Programming Using Java
Generates:
1. Campus_Peer_to_Peer_Marketplace_Project_Report.docx
2. docs/CAMPUS_PEER_TO_PEER_MARKETPLACE_COMPLETE_PROJECT_REPORT.md
"""

import os
import sys
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)

def set_table_borders(table, color="D1D5DB", sz="4", val="single"):
    tblPr = table._tbl.tblPr
    tblBorders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'<w:top w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:bottom w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:left w:val="none"/>'
        f'<w:right w:val="none"/>'
        f'<w:insideH w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:insideV w:val="none"/>'
        f'</w:tblBorders>'
    )
    tblPr.append(tblBorders)

class AcademicReportBuilder:
    def __init__(self):
        self.doc = Document()
        self.md_lines = []
        self._setup_page_format()

    def _setup_page_format(self):
        # 1 inch margins all around
        for section in self.doc.sections:
            section.top_margin = Inches(1.0)
            section.bottom_margin = Inches(1.0)
            section.left_margin = Inches(1.0)
            section.right_margin = Inches(1.0)
            section.header_distance = Inches(0.5)
            section.footer_distance = Inches(0.5)

    def add_page_break(self):
        self.doc.add_page_break()
        self.md_lines.append("\n---\n")

    def add_title(self, text, subtitle_text=""):
        p = self.doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_before = Pt(36)
        p.paragraph_format.space_after = Pt(12)
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(24)
        r.font.bold = True
        r.font.color.rgb = RGBColor(0x1E, 0x3A, 0x8A) # Deep Navy

        self.md_lines.append(f"# {text}\n")

        if subtitle_text:
            p_sub = self.doc.add_paragraph()
            p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_sub.paragraph_format.space_before = Pt(0)
            p_sub.paragraph_format.space_after = Pt(24)
            r_sub = p_sub.add_run(subtitle_text)
            r_sub.font.name = "Arial"
            r_sub.font.size = Pt(13)
            r_sub.font.italic = True
            r_sub.font.color.rgb = RGBColor(0x47, 0x55, 0x69)
            self.md_lines.append(f"### *{subtitle_text}*\n")

    def add_heading_1(self, text):
        p = self.doc.add_paragraph()
        p.paragraph_format.space_before = Pt(20)
        p.paragraph_format.space_after = Pt(8)
        p.paragraph_format.keep_with_next = True
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(16)
        r.font.bold = True
        r.font.color.rgb = RGBColor(0x1E, 0x3A, 0x8A)
        self.md_lines.append(f"\n# {text}\n")

    def add_heading_2(self, text):
        p = self.doc.add_paragraph()
        p.paragraph_format.space_before = Pt(14)
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.keep_with_next = True
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(13)
        r.font.bold = True
        r.font.color.rgb = RGBColor(0x25, 0x63, 0xEB)
        self.md_lines.append(f"\n## {text}\n")

    def add_heading_3(self, text):
        p = self.doc.add_paragraph()
        p.paragraph_format.space_before = Pt(10)
        p.paragraph_format.space_after = Pt(4)
        p.paragraph_format.keep_with_next = True
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(11)
        r.font.bold = True
        r.font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
        self.md_lines.append(f"\n### {text}\n")

    def add_paragraph(self, text, bold_prefix="", italic=False):
        p = self.doc.add_paragraph()
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.line_spacing = 1.15
        
        md_part = ""
        if bold_prefix:
            r_pre = p.add_run(bold_prefix)
            r_pre.font.name = "Times New Roman"
            r_pre.font.size = Pt(11)
            r_pre.font.bold = True
            r_pre.font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
            md_part += f"**{bold_prefix}** "
            
        r = p.add_run(text)
        r.font.name = "Times New Roman"
        r.font.size = Pt(11)
        r.font.italic = italic
        r.font.color.rgb = RGBColor(0x1E, 0x29, 0x3B)
        md_part += text
        self.md_lines.append(f"{md_part}\n")

    def add_bullet(self, text, bold_prefix=""):
        p = self.doc.add_paragraph(style='List Bullet')
        p.paragraph_format.space_after = Pt(3)
        p.paragraph_format.line_spacing = 1.15
        
        md_part = "- "
        if bold_prefix:
            r_pre = p.add_run(bold_prefix)
            r_pre.font.name = "Times New Roman"
            r_pre.font.size = Pt(10.5)
            r_pre.font.bold = True
            r_pre.font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
            md_part += f"**{bold_prefix}** "
            
        r = p.add_run(text)
        r.font.name = "Times New Roman"
        r.font.size = Pt(10.5)
        r.font.color.rgb = RGBColor(0x1E, 0x29, 0x3B)
        md_part += text
        self.md_lines.append(f"{md_part}")

    def add_table(self, headers, rows, col_widths=None):
        table = self.doc.add_table(rows=len(rows) + 1, cols=len(headers))
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        set_table_borders(table)

        # Header Row
        hdr_cells = table.rows[0].cells
        for idx, title in enumerate(headers):
            hdr_cells[idx].text = title
            set_cell_background(hdr_cells[idx], "1E3A8A") # Navy
            set_cell_margins(hdr_cells[idx], top=120, bottom=120, left=140, right=140)
            p = hdr_cells[idx].paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            for r in p.runs:
                r.font.name = "Arial"
                r.font.size = Pt(9.5)
                r.font.bold = True
                r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

        # Data Rows
        for r_idx, row_data in enumerate(rows):
            row_cells = table.rows[r_idx + 1].cells
            bg_color = "FFFFFF" if r_idx % 2 == 0 else "F8FAFC"
            for c_idx, val in enumerate(row_data):
                row_cells[c_idx].text = str(val)
                set_cell_background(row_cells[c_idx], bg_color)
                set_cell_margins(row_cells[c_idx], top=90, bottom=90, left=140, right=140)
                p = row_cells[c_idx].paragraphs[0]
                for r in p.runs:
                    r.font.name = "Arial"
                    r.font.size = Pt(9)
                    r.font.color.rgb = RGBColor(0x1E, 0x29, 0x3B)

        # Apply Column Widths if provided
        if col_widths and len(col_widths) == len(headers):
            for row in table.rows:
                for idx, w in enumerate(col_widths):
                    row.cells[idx].width = Inches(w)

        p_spacer = self.doc.add_paragraph()
        p_spacer.paragraph_format.space_after = Pt(6)

        # Build Markdown Table
        md_header = "| " + " | ".join(headers) + " |"
        md_sep = "| " + " | ".join(["---"] * len(headers)) + " |"
        self.md_lines.append("\n" + md_header + "\n" + md_sep)
        for r in rows:
            self.md_lines.append("| " + " | ".join(str(c).replace("\n", " ") for c in r) + " |")
        self.md_lines.append("\n")

    def add_callout(self, text, title="NOTE / ARCHITECTURAL PRINCIPLE", color_hex="1E3A8A", bg_hex="EFF6FF"):
        tbl = self.doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        tbl.autofit = False
        tbl.columns[0].width = Inches(6.5)
        
        cell = tbl.cell(0, 0)
        set_cell_background(cell, bg_hex)
        set_cell_margins(cell, top=140, bottom=140, left=200, right=160)
        
        tcPr = cell._tc.get_or_add_tcPr()
        tcBorders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:top w:val="none"/>'
            f'<w:left w:val="single" w:sz="24" w:space="0" w:color="{color_hex}"/>'
            f'<w:bottom w:val="none"/>'
            f'<w:right w:val="none"/>'
            f'</w:tcBorders>'
        )
        tcPr.append(tcBorders)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(4)
        run_title = p.add_run(f"📌 {title}\n")
        run_title.font.name = "Arial"
        run_title.font.size = Pt(10)
        run_title.font.bold = True
        run_title.font.color.rgb = RGBColor.from_string(color_hex)
        
        run_body = p.add_run(text)
        run_body.font.name = "Arial"
        run_body.font.size = Pt(9.5)
        run_body.font.color.rgb = RGBColor(0x33, 0x41, 0x55)
        
        p_spacer = self.doc.add_paragraph()
        p_spacer.paragraph_format.space_after = Pt(6)

        self.md_lines.append(f"\n> **{title}**\n> {text}\n")

    def add_code_block(self, code_str, caption=""):
        if caption:
            p_cap = self.doc.add_paragraph()
            p_cap.paragraph_format.space_before = Pt(8)
            p_cap.paragraph_format.space_after = Pt(2)
            r_cap = p_cap.add_run(f"Listing: {caption}")
            r_cap.font.name = "Arial"
            r_cap.font.size = Pt(9)
            r_cap.font.bold = True
            r_cap.font.color.rgb = RGBColor(0x47, 0x55, 0x69)
            self.md_lines.append(f"\n*Listing: {caption}*")
            
        tbl = self.doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        tbl.autofit = False
        tbl.columns[0].width = Inches(6.5)
        
        cell = tbl.cell(0, 0)
        set_cell_background(cell, "F8FAFC")
        set_cell_margins(cell, top=120, bottom=120, left=160, right=160)
        
        tcPr = cell._tc.get_or_add_tcPr()
        tcBorders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:top w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>'
            f'<w:left w:val="single" w:sz="18" w:space="0" w:color="3B82F6"/>'
            f'<w:bottom w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>'
            f'<w:right w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>'
            f'</w:tcBorders>'
        )
        tcPr.append(tcBorders)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(0)
        p.paragraph_format.line_spacing = 1.05
        run = p.add_run(code_str.strip())
        run.font.name = "Consolas"
        run.font.size = Pt(8.5)
        run.font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
        
        p_spacer = self.doc.add_paragraph()
        p_spacer.paragraph_format.space_after = Pt(6)

        self.md_lines.append(f"\n```java\n{code_str.strip()}\n```\n")

    def save(self, docx_path, md_path):
        self.doc.save(docx_path)
        with open(md_path, "w", encoding="utf-8") as f:
            f.write("\n".join(self.md_lines))
        print(f"✅ Generated Word Report: {docx_path}")
        print(f"✅ Generated Markdown Report: {md_path}")

print("AcademicReportBuilder initialized.")
