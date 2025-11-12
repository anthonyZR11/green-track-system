import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType, Chart, registerables } from 'chart.js';
import { EquipmentService, StatusEquiment } from '../services/equipment.service';
import { LoanService } from '../services/loan.service';

// Registrar todos los componentes de Chart.js
Chart.register(...registerables);

@Component({
  selector: 'app-charts',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './charts.component.html',
  styleUrl: './charts.component.css'
})
export class ChartsComponent implements OnInit, AfterViewInit {

  @ViewChild('equipmentChart') equipmentChart!: BaseChartDirective;
  @ViewChild('loanChart') loanChart!: BaseChartDirective;

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'top',
      }
    }
  };

  public equipmentPieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [{ data: [] }]
  };
  public loanPieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [{ data: [] }]
  };

  public pieChartType: ChartType = 'pie';

  showEquipmentChart: boolean = false;
  showLoanChart: boolean = false;

  constructor(private equipmentService: EquipmentService, private loanService: LoanService, private cdRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadEquipmentStats();
    this.loadLoanStats();
  }

  ngAfterViewInit(): void {
    this.loadEquipmentStats();
    setTimeout(() => {
      if (this.equipmentChart?.chart) {
        this.equipmentChart.chart.update();
      }
      this.cdRef.detectChanges();
    }, 0);
  }

  loadEquipmentStats() {
    this.equipmentService.getEquipments('', '', '').subscribe({
      next: (equipments) => {
        const statusCounts = this.countStatus(equipments, 'status');
        this.equipmentPieChartData.labels = Object.keys(statusCounts);
        this.equipmentPieChartData.datasets[0].data = Object.values(statusCounts);
        this.equipmentPieChartData.datasets[0].backgroundColor = this.generateColors(Object.keys(statusCounts).length);

        this.showEquipmentChart = true;  // Mostrar el div y canvas ahora
        setTimeout(() => {
          setTimeout(() => {
            if (this.equipmentChart && this.equipmentChart.chart) {
              this.equipmentChart.chart.update();
            }
          }, 0);
        }, 0);
      },
      error: (err) => {
        console.error('Error al cargar estadísticas de equipos:', err);
      }
    });
  }

  loadLoanStats() {
    this.loanService.getLoans(undefined, undefined, undefined).subscribe({
      next: (loans) => {
        const statusCounts = this.countStatus(loans, 'status');
        this.loanPieChartData.labels = Object.keys(statusCounts);
        this.loanPieChartData.datasets[0].data = Object.values(statusCounts);
        this.loanPieChartData.datasets[0].backgroundColor = this.generateColors(Object.keys(statusCounts).length, ['#8BC34A', '#FF9800', '#03A9F4']);

        this.showLoanChart = true;  // Mostrar el div y canvas ahora
      },
      error: (err) => {
        console.error('Error al cargar estadísticas de préstamos:', err);
      }
    });
  }

  private countStatus(items: any[], property: string): { [key: string]: number } {
    const counts: { [key: string]: number } = {};
    for (const item of items) {
      counts[item[property]] = (counts[item[property]] || 0) + 1;
    }
    return counts;
  }

  private generateColors(numColors: number, baseColors?: string[]): string[] {
    const colors: string[] = [];
    const defaultColors = ['#4CAF50', '#FFC107', '#F44336', '#2196F3', '#9C27B0'];
    const chosenColors = baseColors || defaultColors;

    for (let i = 0; i < numColors; i++) {
      colors.push(chosenColors[i % chosenColors.length]);
    }
    return colors;
  }
}
